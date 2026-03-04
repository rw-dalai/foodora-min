package at.spengergasse.foodoramin.mapper;

import at.spengergasse.foodoramin.model.entity.Order;
import at.spengergasse.foodoramin.model.entity.OrderItem;
import at.spengergasse.foodoramin.model.valueobject.Money;
import at.spengergasse.foodoramin.viewmodel.OrderItemResponse;
import at.spengergasse.foodoramin.viewmodel.OrderResponse;
import at.spengergasse.foodoramin.viewmodel.OrderSummaryResponse;
import java.util.List;

public final class OrderMapper {

  private OrderMapper() {}

  public static OrderSummaryResponse toSummary(Order order) {
    // TODO: implement
    return null;
  }

  public static OrderResponse toResponse(Order order) {
    // TODO: implement
    return null;
  }

  private static OrderItemResponse toItemResponse(OrderItem item) {
    Money lineTotal = item.getUnitPrice().multiply(item.getQuantity());

    return new OrderItemResponse(
        item.getMenuItemName(),
        item.getQuantity(),
        MoneyMapper.toResponse(item.getUnitPrice()),
        MoneyMapper.toResponse(lineTotal));
  }
}
