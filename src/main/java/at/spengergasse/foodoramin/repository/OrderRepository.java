package at.spengergasse.foodoramin.repository;

import at.spengergasse.foodoramin.model.entity.Order;
import java.util.List;

import at.spengergasse.foodoramin.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
   List<Order> findByUserId(Long userId);
//  List<Order> findByUser(User user);
}
