package com.example.pandemic.domain.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.pandemic.application.action.DrawStepAction;
import com.example.pandemic.application.action.PassAction;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.card.InfectionCard;
import com.example.pandemic.domain.card.MemoryCardDeck;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.exception.InvalidActionRequest;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.support.GameBuilder;
import org.junit.jupiter.api.Test;

public class DrawStepTests {

  private final DrawStep drawStep = new DrawStep();

  @Test
  public void shouldDrawOneCardWhenDrawingCityCard() {
    var playerDeck = MemoryCardDeck.<PlayerCard>init();
    playerDeck.addCard(PlayerCard.createCityCard(City.Name.MIAMI));

    var game = GameBuilder.aGame().withPlayerDeck(playerDeck).build();
    var actionRequest = new DrawStepAction();

    var result = drawStep.executeAction(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getCardsInHand().size()).isEqualTo(1);
    assertThat(game.getPlayerDeck().size()).isEqualTo(0);
  }

  @Test
  public void shouldFailWhenPlayerDeckIsEmpty() {
    var game = GameBuilder.aGame().withPlayerDeck(MemoryCardDeck.init()).build();
    var actionRequest = new DrawStepAction();

    var result = drawStep.executeAction(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
    assertThat(game.getState()).isEqualTo(Game.State.LOSE.name());
  }

  @Test
  public void shouldInfectCityWhenDrawingEpidemicCard() {
    // Given
    var playerDeck = MemoryCardDeck.<PlayerCard>init();
    playerDeck.addCard(PlayerCard.createEpidemicCard());

    var infectionDeck = MemoryCardDeck.<InfectionCard>init();
    infectionDeck.addCard(new InfectionCard(City.Name.ATLANTA));
    infectionDeck.addCard(new InfectionCard(City.Name.BAGHDAD));

    var infectionDiscardPile = MemoryCardDeck.<InfectionCard>init();
    infectionDiscardPile.addCard(new InfectionCard(City.Name.PARIS));

    var game =
        GameBuilder.aGame()
            .withInfectionRateMarkerPosition(2)
            .withInfectionDeck(infectionDeck)
            .withInfectionDiscardPile(infectionDiscardPile)
            .withPlayerDeck(playerDeck)
            .build();
    var actionRequest = new DrawStepAction();

    // When
    var result = drawStep.executeAction(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isTrue();
    // player dont have drawn epidemic card
    assertThat(game.getActivePlayer().getCardsInHand().size()).isEqualTo(0);
    // game infection rate track moves forward
    assertThat(game.getInfectionRate()).isEqualTo(3);
    // discard pile is empty - shuffled on top of infection deck
    assertThat(game.getInfectionDiscardPile().size()).isEqualTo(0);
    assertThat(game.getInfectionDeck().size()).isEqualTo(3);
    // on bottom of infection deck should stay BAGHDAD card
    assertThat(game.getInfectionDeck().drawBottomCard().isCityCard(City.Name.BAGHDAD)).isTrue();
    // city atlanta infected by infection card from bottom of infection deck
    assertThat(game.cities().get(City.Name.ATLANTA).getDiseases().get(Disease.Color.BLUE))
        .isEqualTo(1);
  }

  @Test
  public void shouldThrowInvalidActionRequestWhenPassingInvalidAction() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new PassAction();

    assertThrows(InvalidActionRequest.class, () -> drawStep.executeAction(game, actionRequest));
  }
}
