package at.spengergasse.foodoramin.mapper;

import at.spengergasse.foodoramin.model.valueobject.Money;
import at.spengergasse.foodoramin.viewmodel.MoneyResponse;

public final class MoneyMapper {

  private MoneyMapper() {}

  public static MoneyResponse toResponse(Money money) {
    return new MoneyResponse(money.getAmount());
  }
}
