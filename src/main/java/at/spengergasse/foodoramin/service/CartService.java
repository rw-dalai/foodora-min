package at.spengergasse.foodoramin.service;

import at.spengergasse.foodoramin.exception.ApplicationException;
import at.spengergasse.foodoramin.mapper.CartMapper;
import at.spengergasse.foodoramin.model.entity.Cart;
import at.spengergasse.foodoramin.model.entity.MenuItem;
import at.spengergasse.foodoramin.model.entity.User;
import at.spengergasse.foodoramin.repository.CartRepository;
import at.spengergasse.foodoramin.repository.MenuItemRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import at.spengergasse.foodoramin.viewmodel.AddCartItemRequest;
import at.spengergasse.foodoramin.viewmodel.CartResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

  private final UserRepository userRepository;
  private final MenuItemRepository menuItemRepository;
  private final CartRepository cartRepository;

  @Transactional
  public CartResponse addItemToCart(Long userId, AddCartItemRequest request) {
    log.debug("Adding item to cart userId={} menuItemId={} quantity={}",
        userId, request.menuItemId(), request.quantity());

    // Step 1: Load entities
    User user = userRepository.findById(userId)
        .orElseThrow(() -> ApplicationException.ofNotFound("User not found: " + userId));

    MenuItem menuItem = menuItemRepository.findById(request.menuItemId())
        .orElseThrow(() -> ApplicationException.ofNotFound("Menu item not found: " + request.menuItemId()));

    Cart cart = cartRepository.findByUserId(userId)
        .orElseGet(() -> new Cart(user, menuItem.getRestaurant()));

    // Step 2: Validate
    if (!cart.isOwnedBy(userId)) {
      throw ApplicationException.ofForbidden("This cart does not belong to the requested user");
    }
    if (!cart.isForRestaurant(menuItem.getRestaurant().getId())) {
      throw ApplicationException.ofConflict("Cart can only contain items from one restaurant");
    }

    // Step 3: Business logic
    cart.addOrIncreaseItem(menuItem, request.quantity());

    // Step 4: Save and return
    Cart saved = cartRepository.save(cart);
    CartResponse response = CartMapper.toResponse(saved);

    log.info("Added item to cart userId={} cartId={}", userId, saved.getId());
    return response;
  }

  @Transactional
  public void clearCart(Long userId) {
    log.debug("Clearing cart for userId={}", userId);

    // TODO Step 1: Load the user (throw not found if missing)
    // TODO Step 2: Load the user's cart (throw not found if missing)
    // TODO Step 3: Clear all items from the cart
    // TODO Step 4: Log success
  }
}
