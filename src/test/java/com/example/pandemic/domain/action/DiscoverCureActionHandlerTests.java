package com.example.pandemic.domain.action;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.action.DiscoverClueAction;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.support.GameBuilder;
import com.example.pandemic.support.PlayerBuilder;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class DiscoverCureActionHandlerTests {

  private final DiscoverCureActionHandler handler = new DiscoverCureActionHandler();

  @Test
  public void shouldDiscoverCureWithFiveCardsAndEradicatedCuredDiseaseAsThereIsNoDiseaseInCities() {
    // Given
    var cardsUsed =
        Set.of(
            City.Name.SAN_FRANCISCO,
            City.Name.ATLANTA,
            City.Name.ESSEN,
            City.Name.LONDON,
            City.Name.MADRID);
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(cardsUsed.stream().map(PlayerCard::createCityCard).toList())
                    .build())
            .build();
    var actionRequest = new DiscoverClueAction(cardsUsed);

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isTrue();
    assertThat(game.isDiseaseCured(Disease.Color.BLUE)).isTrue();
    assertThat(game.isDiseaseEradicated(Disease.Color.BLUE)).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);

    for (var card : cardsUsed) {
      assertThat(game.getActivePlayer().hasCityCard(card)).isFalse();
    }
  }

  @Test
  public void shouldDiscoverCureWithFourCardsWhenPlayerIsScientist() {
    // Given
    var cardsUsed =
        Set.of(City.Name.SAN_FRANCISCO, City.Name.ATLANTA, City.Name.ESSEN, City.Name.LONDON);
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .asScientist()
                    .withCardsInHand(cardsUsed.stream().map(PlayerCard::createCityCard).toList())
                    .build())
            .build();
    var actionRequest = new DiscoverClueAction(cardsUsed);
    game.cities().get(City.Name.ATLANTA).addDisease(1);

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isTrue();
    assertThat(game.isDiseaseCured(Disease.Color.BLUE)).isTrue();
    assertThat(game.isDiseaseEradicated(Disease.Color.BLUE)).isFalse();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);

    for (var card : cardsUsed) {
      assertThat(game.getActivePlayer().hasCityCard(card)).isFalse();
    }
  }

  @Test
  public void shouldNotDiscoverCureWhenCityHasNoResearchStation() {
    // Given
    var cardsUsed =
        Set.of(
            City.Name.SAN_FRANCISCO,
            City.Name.ATLANTA,
            City.Name.ESSEN,
            City.Name.LONDON,
            City.Name.MADRID);
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .inCity(City.Name.ALGIERS)
                    .withCardsInHand(cardsUsed.stream().map(PlayerCard::createCityCard).toList())
                    .build())
            .build();
    var actionRequest = new DiscoverClueAction(cardsUsed);

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("City has no research station");
  }

  @Test
  public void shouldNotDiscoverCureWhenNotEnoughCardsGiven() {
    // Given
    var cardsUsed =
        Set.of(City.Name.SAN_FRANCISCO, City.Name.ATLANTA, City.Name.ESSEN, City.Name.LONDON);
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(cardsUsed.stream().map(PlayerCard::createCityCard).toList())
                    .build())
            .build();
    var actionRequest = new DiscoverClueAction(cardsUsed);

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Invalid number of cards were given - required 5 cards");
  }

  @Test
  public void shouldNotDiscoverCureWhenMoreThanRequiredCardsWasGiven() {
    // Given
    var cardsUsed =
        Set.of(
            City.Name.SAN_FRANCISCO,
            City.Name.ATLANTA,
            City.Name.ESSEN,
            City.Name.LONDON,
            City.Name.MADRID,
            City.Name.PARIS);
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(cardsUsed.stream().map(PlayerCard::createCityCard).toList())
                    .build())
            .build();
    var actionRequest = new DiscoverClueAction(cardsUsed);

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Invalid number of cards were given - required 5 cards");
  }

  @Test
  public void shouldNotDiscoverCureWhenPlayerHasNotEnoughCards() {
    // Given
    var cardsUsed =
        Set.of(
            City.Name.SAN_FRANCISCO,
            City.Name.ATLANTA,
            City.Name.ESSEN,
            City.Name.LONDON,
            City.Name.MADRID);
    var game = GameBuilder.aGame().build();
    var actionRequest = new DiscoverClueAction(cardsUsed);

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Player has not enough cards");
  }

  @Test
  public void shouldNotDiscoverCureWhenCardsAreNotInSameColor() {
    // Given
    var cardsUsed =
        Set.of(
            City.Name.SAN_FRANCISCO,
            City.Name.ATLANTA,
            City.Name.ESSEN,
            City.Name.DELHI,
            City.Name.MADRID);
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(cardsUsed.stream().map(PlayerCard::createCityCard).toList())
                    .build())
            .build();
    var actionRequest = new DiscoverClueAction(cardsUsed);

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Invalid cards were given");
  }

  @Test
  public void shouldNotDiscoverCureWhenPlayerHasNoRequiredCard() {
    // Given
    var cardsUsed =
        Set.of(
            City.Name.SAN_FRANCISCO,
            City.Name.ATLANTA,
            City.Name.ESSEN,
            City.Name.LONDON,
            City.Name.MADRID);
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(
                        List.of(
                            PlayerCard.createCityCard(City.Name.ATLANTA),
                            PlayerCard.createCityCard(City.Name.ESSEN),
                            PlayerCard.createCityCard(City.Name.LONDON),
                            PlayerCard.createCityCard(City.Name.MADRID),
                            PlayerCard.createCityCard(City.Name.BAGHDAD)))
                    .build())
            .build();
    var actionRequest = new DiscoverClueAction(cardsUsed);

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Player has no required card - SAN_FRANCISCO");
  }

  @Test
  public void shouldDiscoverCureAndRemoveAllDiseaseFromCurrentMedicLocation() {
    // Given
    var cardsUsed =
        Set.of(
            City.Name.SAN_FRANCISCO,
            City.Name.ATLANTA,
            City.Name.ESSEN,
            City.Name.LONDON,
            City.Name.MADRID);
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .asMedic()
                    .withCardsInHand(cardsUsed.stream().map(PlayerCard::createCityCard).toList())
                    .build())
            .build();
    var actionRequest = new DiscoverClueAction(cardsUsed);
    game.cities().get(City.Name.ATLANTA).addDisease(2);

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isTrue();
    assertThat(game.isDiseaseCured(Disease.Color.BLUE)).isTrue();
    assertThat(game.cities().get(City.Name.ATLANTA).getDiseases().get(Disease.Color.BLUE)).isEqualTo(0);
    assertThat(game.isDiseaseEradicated(Disease.Color.BLUE)).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
  }
}
