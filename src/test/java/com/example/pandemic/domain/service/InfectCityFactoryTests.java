package com.example.pandemic.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.domain.card.InfectionCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import com.example.pandemic.support.GameBuilder;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class InfectCityFactoryTests {

  @Test
  public void shouldInfectCityWithCityName() {
    // Given
    var infector = InfectCityFactory.createInfector();
    var game = GameBuilder.aGame().build();
    var infectedCity = City.Name.BAGHDAD;

    // When
    infector.infect(game, infectedCity, infectedCity.getColor());

    // Then
    var cityDiseases = game.cities().get(infectedCity).getDiseases();

    assertThat(cityDiseases.get(infectedCity.getColor())).isEqualTo(1);
    assertThat(cityDiseases.get(Disease.Color.BLUE)).isEqualTo(0);
    assertThat(cityDiseases.get(Disease.Color.YELLOW)).isEqualTo(0);
    assertThat(cityDiseases.get(Disease.Color.RED)).isEqualTo(0);
  }

  @Test
  public void shouldInfectCityWithInfectionCard() {
    // Given
    var infector = InfectCityFactory.createInfector();
    var game = GameBuilder.aGame().build();
    var infectedCity = City.Name.BAGHDAD;

    var infectionCard = new InfectionCard(infectedCity);

    // When
    infector.infect(game, infectionCard);

    // Then
    var cityDiseases = game.cities().get(infectedCity).getDiseases();

    assertThat(cityDiseases.get(infectedCity.getColor())).isEqualTo(1);
    assertThat(cityDiseases.get(Disease.Color.BLUE)).isEqualTo(0);
    assertThat(cityDiseases.get(Disease.Color.YELLOW)).isEqualTo(0);
    assertThat(cityDiseases.get(Disease.Color.RED)).isEqualTo(0);
  }

  @Test
  public void shouldNoInfectCityWhenDiseaseIsEradicated() {
    // Given
    var infector = InfectCityFactory.createInfector();
    var game = GameBuilder.aGame().withBlackDiseaseEradicated().build();
    var infectedCity = City.Name.BAGHDAD;

    // When
    infector.infect(game, infectedCity, infectedCity.getColor());

    // Then
    var cityDiseases = game.cities().get(infectedCity).getDiseases();

    assertThat(cityDiseases.get(infectedCity.getColor())).isEqualTo(0);
  }

  @Test
  public void shouldInfectCityWithOutbreakAndInfectConnectedCities() {
    // Given
    var game = GameBuilder.aGame().build();
    var infectedCity = game.cities().get(City.Name.BAGHDAD);
    var infector = InfectCityFactory.createInfector();

    infectedCity.addDisease(infectedCity.getColor(), 3);

    // When
    infector.infect(game, infectedCity.getName(), infectedCity.getColor());

    // Then
    assertThat(infectedCity.getDiseases().get(infectedCity.getColor())).isEqualTo(3);
    assertThat(game.getOutbreakTrack().getMarkerPosition()).isEqualTo(1);

    for (var city : CityConnections.getConnections(infectedCity.getName())) {
      assertThat(game.cities().get(city).getDiseases().get(infectedCity.getColor())).isEqualTo(1);
    }
  }

  @Test
  public void shouldMakeOutbreakTwiceAndDoNotInfectCityThatAlreadyHasOutbreak() {
    // Given
    var game = GameBuilder.aGame().build();
    var infectedCity = game.cities().get(City.Name.BAGHDAD);
    var nextInfectedCity = game.cities().get(City.Name.ISTANBUL);
    var infector = InfectCityFactory.createInfector();

    infectedCity.addDisease(infectedCity.getColor(), 3);
    nextInfectedCity.addDisease(infectedCity.getColor(), 3);

    // When
    infector.infect(game, infectedCity.getName(), infectedCity.getColor());

    // Then
    assertThat(infectedCity.getDiseases().get(infectedCity.getColor())).isEqualTo(3);
    assertThat(nextInfectedCity.getDiseases().get(infectedCity.getColor())).isEqualTo(3);
    assertThat(game.getOutbreakTrack().getMarkerPosition()).isEqualTo(2);

    var cityThatBothOutbreakCitiesAreConnected = game.cities().get(City.Name.CAIRO);

    assertThat(cityThatBothOutbreakCitiesAreConnected.getDiseases().get(infectedCity.getColor()))
        .isEqualTo(2);
  }

  @Test
  public void shouldNoInfectCityWhereQuarantineSpecialistIs() {
    // Given
    var game =
        GameBuilder.aGame()
            .withPlayers(Set.of(Player.Role.QUARANTINE_SPECIALIST, Player.Role.RESEARCHER))
            .build();
    var infectedCity = City.Name.ATLANTA;
    var infector = InfectCityFactory.createInfector();

    // When
    infector.infect(game, infectedCity, infectedCity.getColor());

    // Then
    var cityDiseases = game.cities().get(infectedCity).getDiseases();

    assertThat(cityDiseases.get(infectedCity.getColor())).isEqualTo(0);
  }

  @Test
  public void shouldNoInfectConnectedCityWhereQuarantineSpecialistIs() {
    // Given
    var game =
        GameBuilder.aGame()
            .withPlayers(Set.of(Player.Role.QUARANTINE_SPECIALIST, Player.Role.RESEARCHER))
            .build();
    var infectedCity = City.Name.MIAMI;
    var infector = InfectCityFactory.createInfector();

    // When
    infector.infect(game, infectedCity, infectedCity.getColor());

    // Then
    var cityDiseases = game.cities().get(infectedCity).getDiseases();

    assertThat(cityDiseases.get(infectedCity.getColor())).isEqualTo(0);
  }

  @Test
  public void shouldNoInfectCityWhereMedicIsAndWhenDiseaseIsCured() {
    // Given
    var game =
        GameBuilder.aGame().withPlayers(Set.of(Player.Role.MEDIC, Player.Role.RESEARCHER)).build();
    var infectedCity = City.Name.ATLANTA;
    var infector = InfectCityFactory.createInfector();
    game.cureDisease(infectedCity.getColor());

    // When
    infector.infect(game, infectedCity, infectedCity.getColor());

    // Then
    var cityDiseases = game.cities().get(infectedCity).getDiseases();

    assertThat(cityDiseases.get(infectedCity.getColor())).isEqualTo(0);
  }
}
