package at.spengergasse.foodoramin.model.entity;

import at.spengergasse.foodoramin.model.Guard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

  @Column(nullable = false, length = 100)
  private String displayName;

  protected User() {
  }

  public User(String displayName) {
    this.displayName = Guard.notBlank(displayName, "displayName is required");
  }

  public void rename(String newDisplayName) {
    this.displayName = Guard.notBlank(newDisplayName, "displayName is required");
  }
}
