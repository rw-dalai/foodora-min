package at.spengergasse.foodoramin.repository;

import at.spengergasse.foodoramin.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
