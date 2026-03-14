package at.spengergasse.foodoramin.service;

import at.spengergasse.foodoramin.exception.ApplicationException;
import at.spengergasse.foodoramin.model.entity.Cart;
import at.spengergasse.foodoramin.model.entity.Order;
import at.spengergasse.foodoramin.model.entity.User;
import at.spengergasse.foodoramin.repository.CartRepository;
import at.spengergasse.foodoramin.repository.OrderRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import at.spengergasse.foodoramin.viewmodel.OrderResponse;
import at.spengergasse.foodoramin.viewmodel.OrderSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.spengergasse.foodoramin.mapper.OrderMapper;
import at.spengergasse.foodoramin.model.enums.OrderStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private final UserRepository userRepository;
  private final CartRepository cartRepository;
  private final OrderRepository orderRepository;

  @Transactional(readOnly = true)
  public List<OrderSummaryResponse> getOrdersForUser(Long userId) {
    log.debug("Loading orders for userId={}", userId);

    // TODO Step 1: Check if the user exists (throw not found if missing)
    if (!userRepository.existsById(userId)) {
        throw ApplicationException.ofNotFound("User not found: " + userId);
    }
//    User user = userRepository.findById(userId).orElseThrow(() ->
//        ApplicationException.ofNotFound("User not found: " + userId));

    // TODO Step 2: Load all orders for the user, filter out CANCELLED,
    //  sort by orderedAt descending, and map to summaries
    List<Order> orders = orderRepository.findByUserId(userId);

    // filter, map, sorted
    List<OrderSummaryResponse> response = orders.stream()
          .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
            .sorted((order1, order2) -> order2.getOrderedAt().compareTo(order1.getOrderedAt()))
          // .sorted(Comparator.comparing(Order::getOrderedAt).reversed())
          .map(order -> OrderMapper.toSummary(order))
          .toList();

    // TODO Step 3: Return the result
    return response;
  }

  @Transactional
  public OrderResponse order(Long userId) {
    log.debug("Placing order for userId={}", userId);

// Imperative Version
//    Optional<User> optionalUser = userRepository.findById(userId);
//    User user;
//    if (optionalUser.isPresent()) {
//      user = optionalUser.get();
//    } else {
//      throw ApplicationException.ofNotFound("User not found " + userId);
//    }

    // TODO Step 1: Load the user (throw not found if missing)
    User user = userRepository.findById(userId).orElseThrow(() ->
        ApplicationException.ofNotFound("User not found " + userId));

    // TODO Step 2: Load the user's cart (throw not found if missing)
    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() ->
        ApplicationException.ofNotFound("Cart not found"));

    // TODO Step 3: Validate the cart is not empty (throw conflict if empty)
    if (cart.isEmpty()) {
      throw ApplicationException.ofConflict("Cart is empty");
    }

    // TODO Step 4: Create an Order, copy lines from cart, save order, clear cart
    var order = new Order(user, cart.getRestaurant());
    order.addLinesFromCart(cart);
    orderRepository.save(order);
    cart.clearItems();
    // TODO Better: Delete the cart for this user

    // TODO Step 5: Return the mapped response
    return OrderMapper.toResponse(order);
  }

  @Transactional
  public void cancelOrder(Long userId, Long orderId) {
    log.debug("Cancelling order orderId={} for userId={}", orderId, userId);

    // TODO Step 1: Load the user (throw not found if missing)
    userRepository.findById(userId).orElseThrow(() ->
        ApplicationException.ofNotFound("User not found " + userId));

    // TODO Step 2: Load the order (throw not found if missing)
    Order order = orderRepository.findById(orderId).orElseThrow(() ->
        ApplicationException.ofNotFound("Order not found " + orderId));

    // TODO Step 3: Verify the order belongs to the user (throw forbidden if not)
    if (!Objects.equals(order.getUser().getId(), userId)) {
      throw ApplicationException.ofForbidden("Order does not belong to user");
    }

    // TODO Step 4: Cancel the order (this may throw IllegalStateException if already DELIVERED)
    order.cancel();

    // TODO Step 5: Log success
    log.info("Cancelled order orderId={} for userId={}", orderId, userId);
  }
}
