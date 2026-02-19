package at.spengergasse.foodoramin.service;

import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newCancelledOrder;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newCartWithOneItem;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newDeliveredOrder;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newRestaurantWithMenu;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newSubmittedOrder;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.spengergasse.foodoramin.model.entity.Order;
import at.spengergasse.foodoramin.repository.CartRepository;
import at.spengergasse.foodoramin.repository.OrderRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
  void getOrdersForUser_shouldFilterCancelledAndSortByIdDesc() {
    // Given
    Long userId = 1L;
    var user = newUser();
    var restaurant = newRestaurantWithMenu();
    // TODO: Why do we need ReflectionTestUtils here? What would be an alternative?
    ReflectionTestUtils.setField(user, "id", userId);
    ReflectionTestUtils.setField(restaurant, "id", 100L);

    var deliveredOrder = newDeliveredOrder(user, restaurant);
    ReflectionTestUtils.setField(deliveredOrder, "id", 3L);

    var submittedOrder = newSubmittedOrder(user, restaurant);
    ReflectionTestUtils.setField(submittedOrder, "id", 2L);

    var cancelledOrder = newCancelledOrder(user, restaurant);
    ReflectionTestUtils.setField(cancelledOrder, "id", 1L);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(orderRepository.findByUserId(userId))
        .thenReturn(List.of(deliveredOrder, submittedOrder, cancelledOrder));

    // When
    var result = orderService.getOrdersForUser(userId);

    // Then — CANCELLED is filtered out, sorted by ID descending
    assertThat(result).hasSize(2);

    assertThat(result.get(0).orderId()).isEqualTo(3L);
    assertThat(result.get(0).status()).isEqualTo("DELIVERED");
    assertThat(result.get(0).restaurantName()).isEqualTo("Fixture Restaurant");
    assertThat(result.get(0).itemCount()).isEqualTo(1);

    assertThat(result.get(1).orderId()).isEqualTo(2L);
    assertThat(result.get(1).status()).isEqualTo("SUBMITTED");
    assertThat(result.get(1).itemCount()).isEqualTo(1);
  }

  @Test
  void order_shouldCreateOrderAndClearCart() {
    // TODO Step 1: Create fixtures (user, restaurant, menuItem, cart) and set IDs via reflection
    // TODO Step 2: Mock the three repositories (findById, findByUserId, save)
    // TODO Step 3: Call orderService.order(userId)
    // TODO Step 4: Assert the response (status, userId, items, menuItemName)
    // TODO Step 5: Verify that order and cart were saved, and cart is now empty
  }
}
