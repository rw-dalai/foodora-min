package at.spengergasse.foodoramin.configuration;

import at.spengergasse.foodoramin.model.entity.Order;
import at.spengergasse.foodoramin.model.entity.Restaurant;
import at.spengergasse.foodoramin.model.entity.User;
import at.spengergasse.foodoramin.model.valueobject.Address;
import at.spengergasse.foodoramin.model.valueobject.Money;
import at.spengergasse.foodoramin.repository.OrderRepository;
import at.spengergasse.foodoramin.repository.RestaurantRepository;
import at.spengergasse.foodoramin.repository.UserRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DemoDataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final RestaurantRepository restaurantRepository;
  private final OrderRepository orderRepository;

  @Override
  public void run(String... args) {
    if (userRepository.count() > 0 || restaurantRepository.count() > 0) {
      return;
    }

    User user = userRepository.save(new User("Alice Student"));

    Restaurant restaurant = new Restaurant("Spenger Pizza", Address.of("Spengergasse 1", "1050", "Wien", "Austria"));
    restaurant.addMenuItem("Margherita", Money.of(new BigDecimal("8.90")));
    restaurant.addMenuItem("Salami", Money.of(new BigDecimal("10.40")));
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    // Seed orders with different statuses for the GET /orders stream demo
    Order order1 = new Order(user, savedRestaurant);
    order1.addLine("Margherita", Money.of(new BigDecimal("8.90")), 2);
    order1.confirm();
    order1.startPreparing();
    order1.deliver();
    orderRepository.save(order1);

    Order order2 = new Order(user, savedRestaurant);
    order2.addLine("Salami", Money.of(new BigDecimal("10.40")), 1);
    order2.confirm();
    orderRepository.save(order2);

    Order order3 = new Order(user, savedRestaurant);
    order3.addLine("Margherita", Money.of(new BigDecimal("8.90")), 1);
    order3.cancel();
    orderRepository.save(order3);

    log.info("Seeded demo data: userId={} restaurantId={} orders=3 (DELIVERED, CONFIRMED, CANCELLED)",
        user.getId(), savedRestaurant.getId());
  }
}
