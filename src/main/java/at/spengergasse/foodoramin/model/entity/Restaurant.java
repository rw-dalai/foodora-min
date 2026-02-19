package at.spengergasse.foodoramin.model.entity;

import at.spengergasse.foodoramin.model.Guard;
import at.spengergasse.foodoramin.model.valueobject.Address;
import at.spengergasse.foodoramin.model.valueobject.Money;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
@Entity
@Table(name = "restaurants")
public class Restaurant extends BaseEntity {

  @Column(nullable = false, length = 150)
  private String name;

  @Embedded
  private Address address;

  @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<MenuItem> menuItems = new ArrayList<>();

  protected Restaurant() {
  }

  public Restaurant(String name, Address address) {
    this.name = Guard.notBlank(name, "restaurant name is required");
    this.address = Guard.notNull(address, "address is required");
  }

  public void rename(String newName) {
    this.name = Guard.notBlank(newName, "restaurant name is required");
  }

  public void changeAddress(Address newAddress) {
    this.address = Guard.notNull(newAddress, "address is required");
  }

  public MenuItem addMenuItem(String itemName, Money price) {
    MenuItem item = new MenuItem(itemName, price);
    item.assignRestaurant(this);
    this.menuItems.add(item);
    return item;
  }

  public List<MenuItem> readMenu() {
    return Collections.unmodifiableList(menuItems);
  }
}
