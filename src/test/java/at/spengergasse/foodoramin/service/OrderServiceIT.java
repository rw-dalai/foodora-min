package at.spengergasse.foodoramin.service;

import at.spengergasse.foodoramin.repository.CartRepository;
import at.spengergasse.foodoramin.repository.OrderRepository;
import at.spengergasse.foodoramin.repository.RestaurantRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import at.spengergasse.foodoramin.model.enums.OrderStatus;

import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderServiceIT {

  @Autowired private OrderService orderService;
  @Autowired private UserRepository userRepository;
  @Autowired private RestaurantRepository restaurantRepository;
  @Autowired private CartRepository cartRepository;
  @Autowired private OrderRepository orderRepository;

  @Test
  @Transactional
  void getOrdersForUser_shouldFilterCancelledAndReturnSummaries() {
    // Given
    var user = userRepository.save(newUser());
    var restaurant = restaurantRepository.save(newRestaurantWithMenu());

    orderRepository.save(newCancelledOrder(user, restaurant));
    orderRepository.save(newSubmittedOrder(user, restaurant));
    orderRepository.save(newDeliveredOrder(user, restaurant));

    // When
    var result = orderService.getOrdersForUser(user.getId());

    // Then — 3 orders in DB, but CANCELLED is filtered out -> 2 results sorted by orderedAt DESC
    assertThat(result).hasSize(2);
    assertThat(result.getFirst().status()).isEqualTo("DELIVERED");
    assertThat(result.getFirst().restaurantName()).isEqualTo("Fixture Restaurant");
    assertThat(result.getFirst().itemCount()).isEqualTo(1);
    assertThat(result.getFirst().total().amount()).hasToString("23.80");
    assertThat(result.get(1).status()).isEqualTo("SUBMITTED");
  }

  @Test
  @Transactional
  void order_shouldCreateOrderAndClearCart() {
    // TODO Step 1: Save a user, restaurant, and cart with one item to the database
    var user = userRepository.save(newUser());
    var restaurant = restaurantRepository.save(newRestaurantWithMenu());
    var cart = newCartWithOneItem(user, restaurant.readMenu().getFirst(), 2);
    cartRepository.save(cart);

    // TODO Step 2: Call orderService.order(userId)
    var response = orderService.order(user.getId());

    // TODO Step 3: Assert the response (status, userId, items, total)
    assertThat(response.status()).isEqualTo("SUBMITTED");
    assertThat(response.userId()).isEqualTo(user.getId());
    assertThat(response.items()).hasSize(1);
    assertThat(response.items().getFirst().menuItemName()).isEqualTo("Pasta");

    // TODO Step 4: Verify side effects — cart is empty, order exists in DB
    var reloadedCart = cartRepository.findByUserId(user.getId());
    assertThat(reloadedCart).isPresent();
    assertThat(reloadedCart.get().readItems()).isEmpty();

    var orders = orderRepository.findByUserId(user.getId());
    assertThat(orders).hasSize(1);
  }

  @Test
  @Transactional
  void cancelOrder_shouldSetStatusToCancelled() {
    // TODO Step 1: Save a user and restaurant
    var user = userRepository.save(newUser());
    var restaurant = restaurantRepository.save(newRestaurantWithMenu());

    // TODO Step 2: Save a SUBMITTED order (use newSubmittedOrder fixture)
    var order = orderRepository.save(newSubmittedOrder(user, restaurant));

    // TODO Step 3: Call orderService.cancelOrder(userId, orderId)
    orderService.cancelOrder(user.getId(), order.getId());

    // TODO Step 4: Reload the order from DB and assert status is CANCELLED
    var reloaded = orderRepository.findById(order.getId());
    assertThat(reloaded).isPresent();
    assertThat(reloaded.get().getStatus()).isEqualTo(OrderStatus.CANCELLED);
  }
}
