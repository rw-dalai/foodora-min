package at.spengergasse.foodoramin.viewmodel;

public record OrderItemResponse(
    String menuItemName,
    int quantity,
    MoneyResponse unitPrice,
    MoneyResponse lineTotal) {}
