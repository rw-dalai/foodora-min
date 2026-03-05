package at.spengergasse.foodoramin.controller;

import at.spengergasse.foodoramin.service.OrderService;
import at.spengergasse.foodoramin.viewmodel.OrderResponse;
import at.spengergasse.foodoramin.viewmodel.OrderSummaryResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  public List<OrderSummaryResponse> getOrders(@PathVariable Long userId) {
    return orderService.getOrdersForUser(userId);
  }

  // TODO: Endpoint for placing Order
  //    Use suiteable HTTP Verb, URL, and status code

  // TODO: Endpoint for cancel Order
  //  Use suiteable HTTP Verb, URL, and status code
}
