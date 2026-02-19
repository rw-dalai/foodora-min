package at.spengergasse.foodoramin.mapper;

import at.spengergasse.foodoramin.model.entity.Cart;
import at.spengergasse.foodoramin.model.entity.CartItem;
import at.spengergasse.foodoramin.model.valueobject.Money;
import at.spengergasse.foodoramin.viewmodel.CartItemResponse;
import at.spengergasse.foodoramin.viewmodel.CartResponse;
import java.util.List;

public final class CartMapper {

  private CartMapper() {}

  public static CartResponse toResponse(Cart cart) {
    List<CartItemResponse> items = cart.readItems().stream().map(CartMapper::toItemResponse).toList();

    return new CartResponse(
        cart.getId(),
        cart.getUser().getId(),
        cart.getRestaurant().getId(),
        items,
        MoneyMapper.toResponse(cart.total()));
  }

  private static CartItemResponse toItemResponse(CartItem item) {
    Money lineTotal = item.getMenuItem().getPrice().multiply(item.getQuantity());

    return new CartItemResponse(
        item.getMenuItem().getId(),
        item.getMenuItem().getName(),
        item.getQuantity(),
        MoneyMapper.toResponse(item.getMenuItem().getPrice()),
        MoneyMapper.toResponse(lineTotal));
  }
}
