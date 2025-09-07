package com.example.pandemic.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CityTests {

  @Test
  public void shouldAddDisease() {
    var city = prepareCity();

    var wasOutbreak = city.addDisease(Disease.Color.BLACK, 2);

    assertThat(wasOutbreak).isFalse();
    assertThat(city.getDiseases().get(Disease.Color.BLACK)).isEqualTo(2);
    assertThat(city.getDiseases().get(Disease.Color.RED)).isEqualTo(0);
    assertThat(city.getDiseases().get(Disease.Color.YELLOW)).isEqualTo(0);
    assertThat(city.getDiseases().get(Disease.Color.BLUE)).isEqualTo(0);
  }

  @Test
  public void shouldAddDiseaseToExistingDisease() {
    var city = prepareCity();
    var disease = Disease.Color.BLACK;

    city.addDisease(disease, 1);
    var wasOutbreak = city.addDisease(disease, 1);
    var cityDisease = city.getDiseases().get(disease);

    assertThat(wasOutbreak).isFalse();
    assertThat(cityDisease).isEqualTo(2);
  }

  @Test
  public void shouldAddDiseaseToMaximumOfThree() {
    var city = prepareCity();
    var disease = Disease.Color.BLACK;

    city.addDisease(disease, 3);
    var wasOutbreak = city.addDisease(disease, 2);
    var cityDisease = city.getDiseases().get(disease);

    assertThat(wasOutbreak).isTrue();
    assertThat(cityDisease).isEqualTo(3);
  }

  @Test
  public void shouldTreatDisease() {
    var city = prepareCity();
    var disease = Disease.Color.BLACK;

    city.addDisease(disease, 2);
    var cured = city.treatDisease(disease, 1);

    assertThat(cured).isEqualTo(1);
    assertThat(city.getDiseases().get(Disease.Color.BLACK)).isEqualTo(1);
    assertThat(city.getDiseases().get(Disease.Color.RED)).isEqualTo(0);
    assertThat(city.getDiseases().get(Disease.Color.YELLOW)).isEqualTo(0);
    assertThat(city.getDiseases().get(Disease.Color.BLUE)).isEqualTo(0);
  }

  @Test
  public void shouldTreatMaxDiseaseToZero() {
    var city = prepareCity();
    var disease = Disease.Color.BLACK;

    city.addDisease(disease, 2);
    var cured = city.treatDisease(disease, 3);

    var cityDisease = city.getDiseases().get(disease);

    assertThat(cured).isEqualTo(2);
    assertThat(cityDisease).isEqualTo(0);
  }

  @Test
  public void shouldNotHaveResearchStationByDefault() {
    var city = new City(City.Name.PARIS);

    assertThat(city.hasResearchStation()).isFalse();
  }

  @Test
  public void shouldBuildResearchStationWithSuccess() {
    var city = prepareCity();
    city.buildResearchStation();

    assertThat(city.hasResearchStation()).isTrue();
  }

  @Test
  public void shouldReturnTrueIfHasAnyDisease() {
    var city = prepareCity();
    city.addDisease(Disease.Color.BLUE, 1);

    var hasDisease = city.hasDisease(Disease.Color.BLUE);

    assertThat(hasDisease).isTrue();
  }

  @Test
  public void shouldReturnFalseIfDoNotHaveDisease() {
    var city = prepareCity();

    var hasDisease = city.hasDisease(Disease.Color.BLUE);

    assertThat(hasDisease).isFalse();
  }

  private City prepareCity() {
    return new City(City.Name.ATLANTA);
  }
}
