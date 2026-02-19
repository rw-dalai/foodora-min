package at.spengergasse.foodoramin.model.fixtures;

import at.spengergasse.foodoramin.model.entity.Cart;
import at.spengergasse.foodoramin.model.entity.MenuItem;
import at.spengergasse.foodoramin.model.entity.Order;
import at.spengergasse.foodoramin.model.entity.Restaurant;
import at.spengergasse.foodoramin.model.entity.User;
import at.spengergasse.foodoramin.model.valueobject.Address;
import at.spengergasse.foodoramin.model.valueobject.Money;
import at.spengergasse.foodoramin.viewmodel.AddCartItemRequest;
import java.math.BigDecimal;

public final class FoodoraFixtures {

  private FoodoraFixtures() {}

  public static User newUser() {
    return new User("Test Student");
  }

  public static Restaurant newRestaurantWithMenu() {
    Restaurant restaurant = new Restaurant("Fixture Restaurant", Address.of("Teststrasse 1", "1050", "Wien", "Austria"));
    restaurant.addMenuItem("Pasta", Money.of(new BigDecimal("11.90")));
    restaurant.addMenuItem("Pizza", Money.of(new BigDecimal("9.50")));
    return restaurant;
  }

  public static AddCartItemRequest addItemRequest(Long menuItemId, int quantity) {
    return new AddCartItemRequest(menuItemId, quantity);
  }

  public static Cart newCartWithOneItem(User user, MenuItem menuItem, int quantity) {
    Cart cart = new Cart(user, menuItem.getRestaurant());
    cart.addOrIncreaseItem(menuItem, quantity);
    return cart;
  }

  public static Order newSubmittedOrder(User user, Restaurant restaurant) {
    Order order = new Order(user, restaurant);
    order.addLine("Pasta", Money.of(new BigDecimal("11.90")), 2);
    return order;
  }

  public static Order newDeliveredOrder(User user, Restaurant restaurant) {
    Order order = newSubmittedOrder(user, restaurant);
    order.confirm();
    order.startPreparing();
    order.deliver();
    return order;
  }

  public static Order newCancelledOrder(User user, Restaurant restaurant) {
    Order order = new Order(user, restaurant);
    order.addLine("Pizza", Money.of(new BigDecimal("9.50")), 1);
    order.cancel();
    return order;
  }
}
