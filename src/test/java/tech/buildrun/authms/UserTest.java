package tech.buildrun.authms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Nested
    class isValidPassword {

        @Test
        @DisplayName("Should Return True When Password Is Valid")
        void shouldReturnTrueWhenPasswordIsValid() {

            // Arrange
            String password = "123";
            var user = new User("admin", password);

            // Act
            var isPasswordValid = user.isValidPassword(password);

            // Assert
            assertTrue(isPasswordValid);
        }

        @Test
        @DisplayName("Should Return False When Password Is Invalid")
        void shouldReturnFalseWhenPasswordIsInvalid() {

            // Arrange
            String password = "123";
            String otherPassword = "567";
            var user = new User("admin", password);

            // Act
            var isPasswordValid = user.isValidPassword(otherPassword);

            // Assert
            assertFalse(isPasswordValid);
        }
    }

    @Nested
    class changePassword {

        @Test
        @DisplayName("Should change password with success")
        void shouldChangePasswordWithSuccess() {

            // Arrange
            String password = "123";
            String newPassword = "456";
            var user = new User("admin", password);

            // Act
            user.changePassword(newPassword);

            // Assert
            assertEquals(newPassword, user.getPassword());
        }

        @Test
        @DisplayName("Should throw exception when password is null")
        void shouldThrowExceptionWhenPasswordIsNull() {

            // Arrange
            String password = "123";
            String newPassword = null;
            var user = new User("admin", password);

            // Act
            var ex = assertThrows(IllegalArgumentException.class, () -> {
                user.changePassword(newPassword);
            });

            // Assert
            assertEquals("A senha não pode ser vazia", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when password is empty")
        void shouldThrowExceptionWhenPasswordIsEmpty() {

            // Arrange
            String password = "123";
            String newPassword = "";
            var user = new User("admin", password);

            // Act
            var ex = assertThrows(IllegalArgumentException.class, () -> {
                user.changePassword(newPassword);
            });

            // Assert
            assertEquals("A senha não pode ser vazia", ex.getMessage());
        }

    }
}