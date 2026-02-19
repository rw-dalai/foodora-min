package at.spengergasse.foodoramin.viewmodel;

import java.util.List;

public record OrderResponse(
    Long orderId,
    Long userId,
    Long restaurantId,
    String status,
    List<OrderItemResponse> items,
    MoneyResponse total) {}
