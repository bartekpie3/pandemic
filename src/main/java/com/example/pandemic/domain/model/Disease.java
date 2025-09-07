package com.example.pandemic.domain.model;

import com.example.pandemic.domain.exception.DiseaseAlreadyIsCured;
import lombok.Getter;

public final class Disease {

  private final Color color;

  @Getter private boolean isEradicated = false;

  @Getter private boolean isCured = false;

  public Disease(Color color) {
    this.color = color;
  }

  public void eradicate() {
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
