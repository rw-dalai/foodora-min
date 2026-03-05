package at.spengergasse.foodoramin.controller;

import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newCancelledOrder;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newCartWithOneItem;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newDeliveredOrder;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newRestaurantWithMenu;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newSubmittedOrder;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newUser;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;

import at.spengergasse.foodoramin.repository.CartRepository;
import at.spengergasse.foodoramin.repository.OrderRepository;
import at.spengergasse.foodoramin.repository.RestaurantRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIT {

  @LocalServerPort private int port;

  @Autowired private UserRepository userRepository;
  @Autowired private RestaurantRepository restaurantRepository;
  @Autowired private CartRepository cartRepository;
  @Autowired private OrderRepository orderRepository;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
    orderRepository.deleteAll();
    cartRepository.deleteAll();
    restaurantRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void getOrders_shouldReturn200WithFilteredOrderList() {
    // Given — 3 orders: cancelled (lowest ID), submitted, delivered (highest ID)
    var user = userRepository.save(newUser());
    var restaurant = restaurantRepository.save(newRestaurantWithMenu());
    orderRepository.save(newCancelledOrder(user, restaurant));
    orderRepository.save(newSubmittedOrder(user, restaurant));
    orderRepository.save(newDeliveredOrder(user, restaurant));

    // When / Then — CANCELLED filtered out, sorted by ID DESC → DELIVERED first
    given()
    .when()
        .get("/api/users/{userId}/orders", user.getId())
    .then()
        .statusCode(200)
        .body("$", hasSize(2))
        .body("[0].status", equalTo("DELIVERED"))
        .body("[0].restaurantName", equalTo("Fixture Restaurant"))
        .body("[1].status", equalTo("SUBMITTED"));
  }

  @Test
  void getOrders_shouldReturn404WhenUserNotFound() {
    given()
    .when()
        .get("/api/users/{userId}/orders", 999L)
    .then()
        .statusCode(404)
        .body("detail", equalTo("User not found: 999"))
        .body("code", equalTo("NOT_FOUND"));
  }

  @Test
  void placeOrder_shouldReturn201AndCreateOrder() {
    // TODO Step 1: Save a user, restaurant, and cart with one item

    // TODO Step 2: POST to /api/users/{userId}/orders and assert 201 + Location header + body
  }

  // ---- cancelOrder ----

  @Test
  void cancelOrder_shouldReturn204ForSubmittedOrder() {
    // TODO Step 1: Save a user, restaurant, and a SUBMITTED order

    // TODO Step 2: POST to /api/users/{userId}/orders/{orderId}/cancel and assert 204
  }

  @Test
  void cancelOrder_shouldReturn404WhenUserNotFound() {
    // TODO Step 1: POST to /api/users/999/orders/999/cancel and assert 404
  }

  @Test
  void cancelOrder_shouldReturn404WhenOrderNotFound() {
    // TODO Step 1: Save a user
  }

  @Test
  void cancelOrder_shouldReturn400WhenOrderAlreadyDelivered() {
    // TODO Step 1: Save a user, restaurant, and a DELIVERED order
  }
}
