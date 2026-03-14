package at.spengergasse.foodoramin.service;

import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newCartWithOneItem;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newRestaurantWithMenu;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newUser;
import static org.assertj.core.api.Assertions.assertThat;

import at.spengergasse.foodoramin.repository.CartRepository;
import at.spengergasse.foodoramin.repository.RestaurantRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CartServiceIT {

  @Autowired private CartService cartService;
  @Autowired private UserRepository userRepository;
  @Autowired private RestaurantRepository restaurantRepository;
  @Autowired private CartRepository cartRepository;

  @Test
  @Transactional
  void clearCart_shouldRemoveAllItemsFromCart() {
    // TODO Step 1: Save a user, restaurant, and a cart with one item
    var user = userRepository.save(newUser());
    var restaurant = restaurantRepository.save(newRestaurantWithMenu());
    var cart = newCartWithOneItem(user, restaurant.readMenu().getFirst(), 2);
    cartRepository.save(cart);

    // TODO Step 2: Call cartService.clearCart(userId)
    cartService.clearCart(user.getId());

    // TODO Step 3: Reload the cart from DB and assert it is empty
    var reloaded = cartRepository.findByUserId(user.getId());
    assertThat(reloaded).isPresent();
    assertThat(reloaded.get().readItems()).isEmpty();

    // TODO Step 4: Assert the cart itself still exists (only items are cleared, not the cart)
    assertThat(cartRepository.findByUserId(user.getId())).isPresent();
  }
}
