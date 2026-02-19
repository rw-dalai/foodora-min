package at.spengergasse.foodoramin.model;

import java.math.BigDecimal;
import org.springframework.util.Assert;

public final class Guard {

  private Guard() {}

  public static <T> T notNull(T value, String message) {
    Assert.notNull(value, message);
    return value;
  }

  public static String notBlank(String value, String message) {
    Assert.hasText(value, message);
    return value.trim();
  }

  public static BigDecimal nonNegative(BigDecimal value, String nullMessage, String rangeMessage) {
    BigDecimal checked = notNull(value, nullMessage);
    Assert.isTrue(checked.signum() >= 0, rangeMessage);
    return checked;
  }

  public static int nonNegative(int value, String message) {
    Assert.isTrue(value >= 0, message);
    return value;
  }
}
