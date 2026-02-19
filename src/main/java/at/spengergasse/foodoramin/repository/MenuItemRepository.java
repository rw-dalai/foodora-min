package at.spengergasse.foodoramin.repository;

import at.spengergasse.foodoramin.model.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
  Page<MenuItem> findByRestaurantId(Long restaurantId, Pageable pageable);
}
