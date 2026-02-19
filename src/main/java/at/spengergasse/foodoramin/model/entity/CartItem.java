package at.spengergasse.foodoramin.model.entity;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cart_id", nullable = false)
  private Cart cart;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "menu_item_id", nullable = false)
  private MenuItem menuItem;

  @Column(nullable = false)
  private int quantity;

  protected CartItem() {
  }

  public CartItem(Cart cart, MenuItem menuItem, int quantity) {
    this.cart = cart;
    this.menuItem = menuItem;
    this.quantity = quantity;
  }

  public void increaseBy(int delta) {
    if (delta <= 0) {
      throw new IllegalArgumentException("delta must be > 0");
    }

    this.quantity += delta;
  }

  public BigDecimal lineAmount() {
    return menuItem.getPrice().getAmount().multiply(BigDecimal.valueOf(quantity));
  }
}
