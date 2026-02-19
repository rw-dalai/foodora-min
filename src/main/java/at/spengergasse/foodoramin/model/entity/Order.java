package at.spengergasse.foodoramin.model.entity;

import at.spengergasse.foodoramin.model.enums.OrderStatus;
import at.spengergasse.foodoramin.model.valueobject.Money;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<OrderItem> items = new ArrayList<>();

  protected Order() {
  }

  public Order(User user, Restaurant restaurant) {
    this.user = user;
    this.restaurant = restaurant;
    this.status = OrderStatus.SUBMITTED;
  }

  public List<OrderItem> readItems() {
    return Collections.unmodifiableList(items);
  }

  public void addLine(String menuItemName, Money unitPrice, int quantity) {
    this.items.add(new OrderItem(this, menuItemName, unitPrice, quantity));
  }

  public void addLinesFromCart(Cart cart) {
    cart.readItems().forEach(item -> addLine(
        item.getMenuItem().getName(), item.getMenuItem().getPrice(), item.getQuantity()));
  }

  public void confirm() {
    if (this.status != OrderStatus.SUBMITTED) {
      throw new IllegalStateException("Can only confirm a SUBMITTED order");
    }
    this.status = OrderStatus.CONFIRMED;
  }

  public void startPreparing() {
    if (this.status != OrderStatus.CONFIRMED) {
      throw new IllegalStateException("Can only start preparing a CONFIRMED order");
    }
    this.status = OrderStatus.PREPARING;
  }

  public void deliver() {
    if (this.status != OrderStatus.PREPARING) {
      throw new IllegalStateException("Can only deliver a PREPARING order");
    }
    this.status = OrderStatus.DELIVERED;
  }

  public void cancel() {
    if (this.status == OrderStatus.DELIVERED) {
      throw new IllegalStateException("Cannot cancel a delivered order");
    }
    this.status = OrderStatus.CANCELLED;
  }

  public Money total() {
    BigDecimal totalAmount = this.items.stream().map(OrderItem::lineAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    return Money.of(totalAmount);
  }
}
