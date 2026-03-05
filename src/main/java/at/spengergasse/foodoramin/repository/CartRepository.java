package at.spengergasse.foodoramin.repository;

import at.spengergasse.foodoramin.model.entity.Cart;

import java.util.List;
import java.util.Optional;

import at.spengergasse.foodoramin.model.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findByUserId(Long userId);
  // List<Cart> findByRestaurant(Restaurant restaurant);
  // List<Cart> findByRestaurantId(Long restaurantId);
}
