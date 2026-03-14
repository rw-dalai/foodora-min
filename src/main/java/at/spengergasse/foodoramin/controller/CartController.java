package at.spengergasse.foodoramin.controller;

import at.spengergasse.foodoramin.service.CartService;
import at.spengergasse.foodoramin.viewmodel.AddCartItemRequest;
import at.spengergasse.foodoramin.viewmodel.CartResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping("/items")
  public CartResponse addItem(@PathVariable Long userId, @Valid @RequestBody AddCartItemRequest request) {
    return cartService.addItemToCart(userId, request);
  }

 // TODO Endpoint for clear Cart
 //  Use suitable HTTP Verb, URL, and status code
  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void clearCart(@PathVariable Long userId) {
    cartService.clearCart(userId);
  }
}
