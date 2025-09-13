package com.example.pandemic.domain.model;

import com.example.pandemic.domain.exception.DiseaseAlreadyIsCured;
import com.example.pandemic.domain.exception.DiseaseAlreadyIsEradicated;
import com.example.pandemic.domain.exception.DiseaseIsNotCured;
import lombok.Getter;

public final class Disease {

  private final Color color;

  @Getter private boolean isEradicated;

  @Getter private boolean isCured;

  public Disease(Color color) {
    this.color = color;
    this.isCured = false;
    this.isEradicated = false;
  }

  public Disease(Color color, boolean isCured, boolean isEradicated) {
    this.color = color;
    this.isCured = isCured;
    this.isEradicated = isEradicated;
  }

  public void eradicate() {
    if (!isCured) {
      throw new DiseaseIsNotCured("Disease is not cured");
    }

    if (isEradicated) {
        throw new DiseaseAlreadyIsEradicated("Disease is already eradicated");
    }

    isEradicated = true;
  }

  public void cure() {
    if (isCured) {
      throw new DiseaseAlreadyIsCured("Disease is already cured");
    }

    isCured = true;
  }

  @Override
  public String toString() {
    return color.name();
  }

  public enum Color {
    RED,
    BLUE,
    YELLOW,
    BLACK
  }
}
