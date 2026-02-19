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
    return new OrderSummaryResponse(
        order.getId(),
        order.getRestaurant().getName(),
        order.getStatus().name(),
        order.readItems().size(),
        MoneyMapper.toResponse(order.total()));
  }

  public static OrderResponse toResponse(Order order) {
    List<OrderItemResponse> items = order.readItems().stream().map(OrderMapper::toItemResponse).toList();

    return new OrderResponse(
        order.getId(),
        order.getUser().getId(),
        order.getRestaurant().getId(),
        order.getStatus().name(),
        items,
        MoneyMapper.toResponse(order.total()));
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
