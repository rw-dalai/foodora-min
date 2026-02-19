package at.spengergasse.foodoramin.model.entity;

import at.spengergasse.foodoramin.model.Guard;
import at.spengergasse.foodoramin.model.valueobject.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "menu_items")
public class MenuItem extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @Column(nullable = false, length = 120)
  private String name;

  @Embedded
  private Money price;

  protected MenuItem() {
  }

  public MenuItem(String name, Money price) {
    this.name = Guard.notBlank(name, "menu item name is required");
    this.price = Guard.notNull(price, "price is required");
  }

  public void rename(String newName) {
    this.name = Guard.notBlank(newName, "menu item name is required");
  }

  public void changePrice(Money newPrice) {
    this.price = Guard.notNull(newPrice, "price is required");
  }

  void assignRestaurant(Restaurant newRestaurant) {
    Guard.notNull(newRestaurant, "restaurant is required");

    if (this.restaurant != null && this.restaurant != newRestaurant) {
      throw new IllegalStateException("menu item already belongs to another restaurant");
    }

    this.restaurant = newRestaurant;
  }
}
