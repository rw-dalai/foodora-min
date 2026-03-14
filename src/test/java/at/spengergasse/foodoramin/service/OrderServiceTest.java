package at.spengergasse.foodoramin.service;

import at.spengergasse.foodoramin.model.entity.Order;
import at.spengergasse.foodoramin.repository.CartRepository;
import at.spengergasse.foodoramin.repository.OrderRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import at.spengergasse.foodoramin.model.entity.Cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private CartRepository cartRepository;
  @Mock private OrderRepository orderRepository;

  private OrderService orderService;

  @BeforeEach
  void setUp() {
    orderService = new OrderService(userRepository, cartRepository, orderRepository);
  }

  @Test
  void getOrdersForUser_shouldFilterCancelledAndSortByOrderedAtDesc() {
    // Given
    Long userId = 1L;
    var user = newUser();
    var restaurant = newRestaurantWithMenu();
    // TODO: Why do we need ReflectionTestUtils here? What would be an alternative?
    ReflectionTestUtils.setField(user, "id", userId);
    ReflectionTestUtils.setField(restaurant, "id", 100L);

    var deliveredOrder = newDeliveredOrder(user, restaurant);
    ReflectionTestUtils.setField(deliveredOrder, "id", 3L);
    ReflectionTestUtils.setField(deliveredOrder, "orderedAt", LocalDateTime.now().minusHours(2));

    var submittedOrder = newSubmittedOrder(user, restaurant);
    ReflectionTestUtils.setField(submittedOrder, "id", 2L);
    ReflectionTestUtils.setField(submittedOrder, "orderedAt", LocalDateTime.now().minusHours(1));

    var cancelledOrder = newCancelledOrder(user, restaurant);
    ReflectionTestUtils.setField(cancelledOrder, "id", 1L);
    ReflectionTestUtils.setField(cancelledOrder, "orderedAt", LocalDateTime.now());

    when(userRepository.existsById(userId)).thenReturn(true);
    when(orderRepository.findByUserId(userId))
        .thenReturn(List.of(deliveredOrder, submittedOrder, cancelledOrder));

    // When
    var result = orderService.getOrdersForUser(userId);

    // Then — CANCELLED is filtered out, sorted by orderedAt descending
    assertThat(result).hasSize(2);

    assertThat(result.get(0).orderId()).isEqualTo(2L);
    assertThat(result.get(0).status()).isEqualTo("SUBMITTED");
    assertThat(result.get(0).itemCount()).isEqualTo(1);

    assertThat(result.get(1).orderId()).isEqualTo(3L);
    assertThat(result.get(1).status()).isEqualTo("DELIVERED");
    assertThat(result.get(1).restaurantName()).isEqualTo("Fixture Restaurant");
    assertThat(result.get(1).itemCount()).isEqualTo(1);
  }

  @Test
  void order_shouldCreateOrderAndClearCart() {
    // TODO Step 1: Create fixtures (user, restaurant, menuItem, cart) and set IDs via reflection
    Long userId = 1L;
    var user = newUser();
    var restaurant = newRestaurantWithMenu();
    var menuItem = restaurant.readMenu().getFirst();
    ReflectionTestUtils.setField(user, "id", userId);
    ReflectionTestUtils.setField(restaurant, "id", 100L);
    ReflectionTestUtils.setField(menuItem, "id", 10L);

    var cart = newCartWithOneItem(user, menuItem, 2);
    ReflectionTestUtils.setField(cart, "id", 50L);

    // TODO Step 2: Mock the three repositories (findById, findByUserId, save)
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
    when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
      Order order = invocation.getArgument(0);
      ReflectionTestUtils.setField(order, "id", 99L);
      return order;
    });

    // TODO Step 3: Call orderService.order(userId)
    var response = orderService.order(userId);

    // TODO Step 4: Assert the response (status, userId, items, menuItemName)
    assertThat(response.status()).isEqualTo("SUBMITTED");
    assertThat(response.userId()).isEqualTo(userId);
    assertThat(response.items()).hasSize(1);
    assertThat(response.items().getFirst().menuItemName()).isEqualTo("Pasta");

    // TODO Step 5: Verify that order and cart were saved, and cart is now empty
    verify(orderRepository).save(any(Order.class));
    assertThat(cart.isEmpty()).isTrue();
  }
}
