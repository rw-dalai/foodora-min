package at.spengergasse.foodoramin.viewmodel;


public record CartItemResponse(
    Long menuItemId,
    String menuItemName,
    int quantity,
    MoneyResponse unitPrice,
    MoneyResponse lineTotal) {}
