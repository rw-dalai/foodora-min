package at.spengergasse.foodoramin.repository;

import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newCartWithOneItem;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newRestaurantWithMenu;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newUser;
import static org.assertj.core.api.Assertions.assertThat;

import at.spengergasse.foodoramin.model.entity.Order;
import at.spengergasse.foodoramin.model.entity.Restaurant;
import at.spengergasse.foodoramin.model.entity.User;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class FoodoraRepositoryIT {

  @Autowired private UserRepository userRepository;
  @Autowired private RestaurantRepository restaurantRepository;
  @Autowired private CartRepository cartRepository;
  @Autowired private OrderRepository orderRepository;
  @Autowired private EntityManager entityManager;

  @Test
  void saveAndRetrieve_shouldWork() {
    // Given
    User user = userRepository.save(newUser());
    Restaurant restaurant = restaurantRepository.save(newRestaurantWithMenu());
    var menuItem = restaurant.readMenu().getFirst();

    var cart = newCartWithOneItem(user, menuItem, 2);
    cartRepository.save(cart);

    Order order = new Order(user, restaurant);
    order.addLinesFromCart(cart);
    orderRepository.save(order);

    entityManager.flush();
    entityManager.clear();

    // When
    var loadedCart = cartRepository.findByUserId(user.getId()).orElseThrow();
    var loadedOrder = orderRepository.findByUserId(user.getId()).getFirst();

    // Then
    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat(loadedCart.getId()).isNotNull();
      softly.assertThat(loadedCart.getUser().getId()).isEqualTo(user.getId());
      softly.assertThat(loadedCart.readItems()).hasSize(1);
      softly.assertThat(loadedCart.readItems().getFirst().getMenuItem().getName()).isEqualTo("Pasta");
      softly.assertThat(loadedCart.readItems().getFirst().getQuantity()).isEqualTo(2);

      softly.assertThat(loadedOrder.getId()).isNotNull();
      softly.assertThat(loadedOrder.getUser().getId()).isEqualTo(user.getId());
      softly.assertThat(loadedOrder.readItems()).hasSize(1);
      softly.assertThat(loadedOrder.readItems().getFirst().getMenuItemName()).isEqualTo("Pasta");
      softly.assertThat(loadedOrder.total().getAmount()).isEqualTo(new BigDecimal("23.80"));
    });
  }
}
