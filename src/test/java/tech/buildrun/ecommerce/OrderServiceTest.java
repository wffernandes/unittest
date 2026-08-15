package tech.buildrun.ecommerce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;

    @InjectMocks
    private OrderService orderService;


    @Nested
    class placeOrder {

        @Test
        @DisplayName("Should place order with success")
        void shouldPlaceOrderWithSuccess() {

            // Arrange
            var dummyOrder = new Order(1, "Bruno", 200.0);

            // Act
            orderService.placeOrder(dummyOrder);

            // Assert
            verify(repository, times(1)).save(orderArgumentCaptor.capture());
            var orderCaptured = orderArgumentCaptor.getValue();
            assertEqualsOrder(dummyOrder, orderCaptured);
        }

        @ParameterizedTest
        @ValueSource(doubles = {0, -2.0, -50.0})
        @DisplayName("Should throw exception when total is below or equal zero")
        void shouldThrowExceptionWhenTotalIsBelowOrEqualZero(double total) {

            // Arrange
            var dummyOrder = new Order(1, "Bruno", total);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                orderService.placeOrder(dummyOrder);
            });

            verify(repository, times(0)).save(any());
        }

        @Test
        @DisplayName("Should throw exception when place order")
        void shouldThrowExceptionWhenPlaceOrder() {

            // Arrange
            var dummyOrder = new Order(1, "Bruno", 200.0);
            doThrow(new RuntimeException()).when(repository).save(any());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                orderService.placeOrder(dummyOrder);
            });
        }
    }

    @Nested
    class getOrder {

        @Test
        @DisplayName("Should return order when exists")
        void shouldReturnOrderWhenExists() {

            // Arrange
            int orderId = 1;
            var dummyOrder = new Order(1, "Bruno", 200.0);
            doReturn(dummyOrder).when(repository).findById(eq(orderId));

            // Act
            var order = orderService.getOrder(orderId);

            // Assert
            assertNotNull(order);
            assertEqualsOrder(dummyOrder, order);
            verify(repository, times(1)).findById(eq(orderId));

        }

        @Test
        @DisplayName("Should return null when order does not exists")
        void shouldReturnNullWhenOrderDoesNotExists() {

            // Arrange
            int orderId = 1;
            doReturn(null).when(repository).findById(eq(orderId));

            // Act
            var order = orderService.getOrder(orderId);

            // Assert
            assertNull(order);
            verify(repository, times(1)).findById(eq(orderId));

        }
    }

    private static void assertEqualsOrder(Order dummyOrder, Order order) {
        assertEquals(dummyOrder.getId(), order.getId());
        assertEquals(dummyOrder.getCustomer(), order.getCustomer());
        assertEquals(dummyOrder.getTotal(), order.getTotal());
    }
}