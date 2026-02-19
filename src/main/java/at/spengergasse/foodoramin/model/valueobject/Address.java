package at.spengergasse.foodoramin.model.valueobject;

import at.spengergasse.foodoramin.model.Guard;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

  @Column(nullable = false, length = 120)
  private String street;

  @Column(nullable = false, length = 20)
  private String postalCode;

  @Column(nullable = false, length = 80)
  private String city;

  @Column(nullable = false, length = 80)
  private String country;

  private Address(String street, String postalCode, String city, String country) {
    this.street = normalize(street, "street");
    this.postalCode = normalize(postalCode, "postalCode");
    this.city = normalize(city, "city");
    this.country = normalize(country, "country");
  }

  public static Address of(String street, String postalCode, String city, String country) {
    return new Address(street, postalCode, city, country);
  }

  private static String normalize(String value, String fieldName) {
    return Guard.notBlank(value, fieldName + " is required");
  }
}
