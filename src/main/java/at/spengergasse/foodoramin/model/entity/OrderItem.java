package at.spengergasse.foodoramin.model.entity;

import at.spengergasse.foodoramin.model.valueobject.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(nullable = false, length = 150)
  private String menuItemName;

  @Embedded
  private Money unitPrice;

  @Column(nullable = false)
  private int quantity;

  protected OrderItem() {
  }

  public OrderItem(Order order, String menuItemName, Money unitPrice, int quantity) {
    this.order = order;
    this.menuItemName = menuItemName;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
  }

  public BigDecimal lineAmount() {
    return unitPrice.getAmount().multiply(BigDecimal.valueOf(quantity));
  }
}
