package at.spengergasse.foodoramin.viewmodel;

import java.util.List;

public record CartResponse(
    Long cartId,
    Long userId,
    Long restaurantId,
    List<CartItemResponse> items,
    MoneyResponse total) {}
