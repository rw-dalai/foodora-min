package at.spengergasse.foodoramin.mapper;

import at.spengergasse.foodoramin.model.entity.MenuItem;
import at.spengergasse.foodoramin.viewmodel.MenuItemResponse;

public final class MenuMapper {

  private MenuMapper() {}

  public static MenuItemResponse toResponse(MenuItem menuItem) {
    return new MenuItemResponse(menuItem.getId(), menuItem.getName(), MoneyMapper.toResponse(menuItem.getPrice()));
  }
}
