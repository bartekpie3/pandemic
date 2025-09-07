package com.example.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;

public class EnumSetValidator implements ConstraintValidator<ValidEnum, Set<String>> {
  private Class<? extends Enum<?>> enumClass;

  @Override
  public void initialize(ValidEnum annotation) {
    this.enumClass = annotation.enumClass();
  }

  @Override
  public boolean isValid(Set<String> values, ConstraintValidatorContext context) {
    if (values == null) return true;

    return values.stream()
        .allMatch(
            value ->
                Arrays.stream(enumClass.getEnumConstants())
                    .anyMatch(e -> e.name().equalsIgnoreCase(value)));
  }
}
