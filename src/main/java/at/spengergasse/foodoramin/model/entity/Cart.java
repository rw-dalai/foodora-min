package at.spengergasse.foodoramin.model.entity;

import at.spengergasse.foodoramin.model.valueobject.Money;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

@Getter
@Entity
@Table(name = "carts", uniqueConstraints = @UniqueConstraint(name = "uk_cart_user", columnNames = "user_id"))
public class Cart extends BaseEntity {

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<CartItem> items = new ArrayList<>();


  // --- JPA ctor ---
  protected Cart() {
  }

  // --- Business ctor ---
  public Cart(User user, Restaurant restaurant) {
    this.user = user;
    this.restaurant = restaurant;
  }

  // --- Business methods ---

  public List<CartItem> readItems() {
    return Collections.unmodifiableList(this.items);
  }

  public boolean isOwnedBy(Long userId) {
    return Objects.equals(this.user.getId(), userId);
  }

  public boolean isForRestaurant(Long restaurantId) {
    return Objects.equals(this.restaurant.getId(), restaurantId);
  }

  public void addOrIncreaseItem(MenuItem menuItem, int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("quantity must be > 0");
    }

    if (!isForRestaurant(menuItem.getRestaurant().getId())) {
      throw new IllegalStateException("cart can contain items from one restaurant only");
    }

    this.items.stream()
        .filter(it -> Objects.equals(it.getMenuItem().getId(), menuItem.getId()))
        .findFirst()
        .ifPresentOrElse(
            existing -> existing.increaseBy(quantity),
            () -> this.items.add(new CartItem(this, menuItem, quantity))
        );
  }

  public boolean isEmpty() {
    return this.items.isEmpty();
  }

  public void clearItems() {
    this.items.clear();
  }

  public int totalQuantity() {
    return this.items.stream().mapToInt(CartItem::getQuantity).sum();
  }

  public Money total() {
    BigDecimal totalAmount =
        this.items.stream().map(CartItem::lineAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

    return Money.of(totalAmount);
  }
}
