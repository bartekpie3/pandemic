package com.example.pandemic.domain.collection;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.domain.model.City;
import java.util.Set;

import com.example.pandemic.domain.model.Disease;
import org.junit.jupiter.api.Test;

public class CitiesTests {

  @Test
  public void shouldReturnTrueWhenCitiesAreInSameColor() {
    var result = Cities.areCitiesInSameColor(Set.of(City.Name.SAN_FRANCISCO, City.Name.ATLANTA));

    assertThat(result).isTrue();
  }

  @Test
  public void shouldReturnFalseWhenCitiesAreNotInSameColor() {
    var result = Cities.areCitiesInSameColor(Set.of(City.Name.SAN_FRANCISCO, City.Name.MIAMI));

    assertThat(result).isFalse();
  }

  @Test
  public void shouldReturnTrueWhenAllCitiesDoNotHasDisease() {
    var isDiseaseEradicated = Cities.init().isDiseaseEradicated(Disease.Color.BLUE);

    assertThat(isDiseaseEradicated).isTrue();
  }

  @Test
  public void shouldReturnFalseWhenAnyCityHasDisease() {
    var cities = Cities.init();
    cities.get(City.Name.ATLANTA).addDisease(2);

    var isDiseaseEradicated = cities.isDiseaseEradicated(Disease.Color.BLUE);

    assertThat(isDiseaseEradicated).isFalse();
  }
}
