package at.spengergasse.foodoramin.repository;

import at.spengergasse.foodoramin.model.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {}
