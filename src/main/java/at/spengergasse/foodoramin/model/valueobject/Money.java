package at.spengergasse.foodoramin.model.valueobject;

import at.spengergasse.foodoramin.model.Guard;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal amount;

  private Money(BigDecimal amount) {
    this.amount = normalize(amount);
  }

  public static Money of(BigDecimal amount) {
    return new Money(amount);
  }

  public Money add(Money other) {
    Money checked = Guard.notNull(other, "other is required");
    return Money.of(this.amount.add(checked.amount));
  }

  public Money multiply(int factor) {
    int checked = Guard.nonNegative(factor, "factor must be >= 0");
    return Money.of(this.amount.multiply(BigDecimal.valueOf(checked)));
  }

  private static BigDecimal normalize(BigDecimal input) {
    return Guard.nonNegative(input, "amount is required", "amount must be >= 0")
        .setScale(2, RoundingMode.HALF_UP);
  }
}
