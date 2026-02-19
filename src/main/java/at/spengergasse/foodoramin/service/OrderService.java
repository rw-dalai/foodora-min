package at.spengergasse.foodoramin.service;

import at.spengergasse.foodoramin.exception.ApplicationException;
import at.spengergasse.foodoramin.mapper.OrderMapper;
import at.spengergasse.foodoramin.model.entity.BaseEntity;
import at.spengergasse.foodoramin.model.entity.Cart;
import at.spengergasse.foodoramin.model.entity.Order;
import at.spengergasse.foodoramin.model.entity.User;
import at.spengergasse.foodoramin.model.enums.OrderStatus;
import at.spengergasse.foodoramin.repository.CartRepository;
import at.spengergasse.foodoramin.repository.OrderRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import at.spengergasse.foodoramin.viewmodel.OrderResponse;
import at.spengergasse.foodoramin.viewmodel.OrderSummaryResponse;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    userRepository.findById(userId)
        .orElseThrow(() -> ApplicationException.ofNotFound("User not found: " + userId));

    List<OrderSummaryResponse> result = orderRepository.findByUserId(userId)
        .stream()
        .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
        .sorted(Comparator.comparing(BaseEntity::getId).reversed())
        .map(OrderMapper::toSummary)
        .toList();

    log.info("Loaded orders for userId={} count={}", userId, result.size());
    return result;
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
