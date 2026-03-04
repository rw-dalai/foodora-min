package at.spengergasse.foodoramin.service;

import at.spengergasse.foodoramin.repository.CartRepository;
import at.spengergasse.foodoramin.repository.OrderRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import at.spengergasse.foodoramin.viewmodel.OrderResponse;
import at.spengergasse.foodoramin.viewmodel.OrderSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // TODO Step 1: Load the user (throw not found if missing)
    // TODO Step 2: Load all orders for the user, filter out CANCELLED, sort by orderedAt descending, and map to summaries
    // TODO Step 3: Return the result

    return null;
  }

  @Transactional
  public OrderResponse order(Long userId) {
    log.debug("Placing order for userId={}", userId);

    // TODO Step 1: Load the user (throw not found if missing)
    // TODO Step 2: Load the user's cart (throw not found if missing)
    // TODO Step 3: Validate the cart is not empty (throw conflict if empty)
    // TODO Step 4: Create an Order, copy lines from cart, save order, clear cart
    // TODO Step 5: Return the mapped response

    return null;
  }
}
