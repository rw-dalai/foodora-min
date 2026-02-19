package at.spengergasse.foodoramin.service;

import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.addItemRequest;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newCartWithOneItem;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newRestaurantWithMenu;
import static at.spengergasse.foodoramin.model.fixtures.FoodoraFixtures.newUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.spengergasse.foodoramin.exception.ApplicationException;
import at.spengergasse.foodoramin.model.entity.Cart;
import at.spengergasse.foodoramin.repository.CartRepository;
import at.spengergasse.foodoramin.repository.MenuItemRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private MenuItemRepository menuItemRepository;
  @Mock private CartRepository cartRepository;

  private CartService cartService;

  @BeforeEach
  void setUp() {
    cartService = new CartService(userRepository, menuItemRepository, cartRepository);
  }

  @Test
  void addItemToCart_shouldCreateCartAndReturnResponse() {
    // Given
    Long userId = 1L;
    Long menuItemId = 10L;

    var user = newUser();
    var restaurant = newRestaurantWithMenu();
    var menuItem = restaurant.readMenu().getFirst();

    // TODO: Why do we need ReflectionTestUtils here? What would be an alternative?
    ReflectionTestUtils.setField(user, "id", userId);
    ReflectionTestUtils.setField(restaurant, "id", 100L);
    ReflectionTestUtils.setField(menuItem, "id", menuItemId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
    when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
    when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
      Cart cart = invocation.getArgument(0);
      ReflectionTestUtils.setField(cart, "id", 50L);
      return cart;
    });

    // When
    var response = cartService.addItemToCart(userId, addItemRequest(menuItemId, 2));

    // Then
    assertThat(response.userId()).isEqualTo(userId);
    assertThat(response.restaurantId()).isEqualTo(100L);
    assertThat(response.items()).hasSize(1);
    assertThat(response.items().getFirst().quantity()).isEqualTo(2);
    verify(cartRepository).save(any(Cart.class));
  }

  @Test
  void addItemToCart_shouldThrowConflict_whenMenuItemBelongsToDifferentRestaurant() {
    // Given
    Long userId = 1L;
    Long menuItemId = 10L;

    var user = newUser();
    var restaurantA = newRestaurantWithMenu();
    var restaurantB = newRestaurantWithMenu();
    var menuItemFromA = restaurantA.readMenu().getFirst();
    var existingCart = newCartWithOneItem(user, restaurantB.readMenu().getFirst(), 1);

    ReflectionTestUtils.setField(user, "id", userId);
    ReflectionTestUtils.setField(restaurantA, "id", 100L);
    ReflectionTestUtils.setField(restaurantB, "id", 200L);
    ReflectionTestUtils.setField(menuItemFromA, "id", menuItemId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItemFromA));
    when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));

    // When / Then
    assertThatThrownBy(() -> cartService.addItemToCart(userId, addItemRequest(menuItemId, 1)))
        .isInstanceOf(ApplicationException.class)
        .satisfies(ex -> assertThat(((ApplicationException) ex).getStatus()).isEqualTo(HttpStatus.CONFLICT));

    verify(cartRepository, never()).save(any());
  }
}
