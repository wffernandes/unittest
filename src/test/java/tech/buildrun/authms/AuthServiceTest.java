package tech.buildrun.authms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private User user;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userAc;

    @InjectMocks
    private AuthService authService;


    @Nested
    class authenticate {

        @Test
        @DisplayName("Should return false when user does not exist")
        void shoudlReturnFalseWhenUserDoesNotExist() {

            // Arrange
            var username = "admin";
            var password = "123";
            doReturn(null).when(userRepository).findByUsername(eq(username));

            // Act
            var isAuth = authService.authenticate(username, password);

            // Assert
            assertFalse(isAuth);
            verify(userRepository, times(1)).findByUsername(eq(username));

        }

        @Test
        @DisplayName("Should return false when user exist and password is invalid")
        void shoudlReturnFalseWhenUserExistAndPasswordIsInvalid() {

            // Arrange
            var username = "admin";
            var password = "123";
            doReturn(user).when(userRepository).findByUsername(eq(username));
            doReturn(false).when(user).isValidPassword(eq(password));

            // Act
            var isAuth = authService.authenticate(username, password);

            // Assert
            assertFalse(isAuth);
            verify(userRepository, times(1)).findByUsername(eq(username));
            verify(user, times(1)).isValidPassword(eq(password));

        }

        @Test
        @DisplayName("Should return true when user exist and password is valid")
        void shoudlReturnTrueWhenUserExistAndPasswordIsValid() {

            // Arrange
            var username = "admin";
            var password = "123";
            doReturn(user).when(userRepository).findByUsername(eq(username));
            doReturn(true).when(user).isValidPassword(eq(password));

            // Act
            var isAuth = authService.authenticate(username, password);

            // Assert
            assertTrue(isAuth);
            verify(userRepository, times(1)).findByUsername(eq(username));
            verify(user, times(1)).isValidPassword(eq(password));

        }
    }

    @Nested
    class register {

        @Test
        @DisplayName("Should register with success when user does not exist")
        void shouldRegisterWithSuccessWhenUserDoesNotExist() {

            // Arrange
            var username = "admin";
            var password = "123";
            doReturn(null).when(userRepository).findByUsername(eq(username));

            // Act
            authService.register(username, password);

            // Assert
            verify(userRepository, times(1)).findByUsername(eq(username));
            verify(userRepository, times(1)).save(userAc.capture());

            var userCaptured = userAc.getValue();
            assertEquals(username, userCaptured.getUsername());
            assertEquals(password, userCaptured.getPassword());

        }

        @Test
        @DisplayName("Should NOT register when user already exist")
        void shouldNotRegisterWhenUserAlreadyExist() {

            // Arrange
            var username = "admin";
            var password = "123";
            doReturn(user).when(userRepository).findByUsername(eq(username));

            // Act & Assert
            var ex = assertThrows(IllegalArgumentException.class, () -> {
                authService.register(username, password);
            });

            // Assert
            assertEquals("Usuário já existe", ex.getMessage());
            verify(userRepository, times(1)).findByUsername(eq(username));
            verify(userRepository, times(0)).save(any());
        }
    }
}