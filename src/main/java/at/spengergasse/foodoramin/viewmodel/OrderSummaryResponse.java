package at.spengergasse.foodoramin.viewmodel;

public record OrderSummaryResponse(
    Long orderId,
    String restaurantName,
    String status,
    int itemCount,
    MoneyResponse total) {}
