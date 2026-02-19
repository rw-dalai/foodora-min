package at.spengergasse.foodoramin.viewmodel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequest(@NotNull Long menuItemId, @Min(1) int quantity) {}
