package at.spengergasse.foodoramin.controller;

import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newCartWithOneItem;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newRestaurantWithMenu;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newUser;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
class CartControllerIT {

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
  void clearCart_shouldReturn204AndEmptyCart() {
    // TODO Step 1: Save a user, restaurant, and cart with one item
    var user = userRepository.save(newUser());
    var restaurant = restaurantRepository.save(newRestaurantWithMenu());
    cartRepository.save(newCartWithOneItem(user, restaurant.readMenu().getFirst(), 2));

    // TODO Step 2: DELETE /api/users/{userId}/cart and assert 204
    given()
    .when()
        .delete("/api/users/{userId}/cart", user.getId())
    .then()
        .statusCode(204);
  }

  @Test
  void clearCart_shouldReturn404WhenUserNotFound() {
    // TODO Step 1: DELETE /api/users/999/cart and assert 404 + error body with code NOT_FOUND
    given()
    .when()
        .delete("/api/users/{userId}/cart", 999L)
    .then()
        .statusCode(404)
        .body("code", equalTo("NOT_FOUND"));
  }
}
