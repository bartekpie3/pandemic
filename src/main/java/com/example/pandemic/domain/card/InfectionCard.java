package com.example.pandemic.domain.card;

import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import lombok.NonNull;

public final class InfectionCard extends Card {

  public InfectionCard(City.@NonNull Name cityName) {
    super(cityName.name());
  }

  public City.Name getCityName() {
    return City.Name.valueOf(name);
  }

  public Disease.Color getDiseaseColor() {
    return City.Name.valueOf(name).getColor();
  }
}
