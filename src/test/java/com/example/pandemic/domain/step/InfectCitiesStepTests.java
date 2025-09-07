package com.example.pandemic.domain.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.pandemic.application.action.DrawStepAction;
import com.example.pandemic.application.action.InfectStepAction;
import com.example.pandemic.domain.card.InfectionCard;
import com.example.pandemic.domain.card.MemoryCardDeck;
import com.example.pandemic.domain.exception.InvalidActionRequest;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.support.GameBuilder;
import org.junit.jupiter.api.Test;

public class InfectCitiesStepTests {

  private final InfectCitiesStep infectCitiesStep = new InfectCitiesStep();

  @Test
  public void shouldInfectCitiesWithSuccess() {
    // Given
    var infectionDeck = MemoryCardDeck.<InfectionCard>init();
    infectionDeck.addCard(new InfectionCard(City.Name.ATLANTA));
    infectionDeck.addCard(new InfectionCard(City.Name.BAGHDAD));

    var game = GameBuilder.aGame().withInfectionDeck(infectionDeck).build();
    var actionRequest = new InfectStepAction();

    // When
    var result = infectCitiesStep.executeAction(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getInfectionDeck().size()).isEqualTo(0);
    assertThat(game.getInfectionDiscardPile().size()).isEqualTo(2);
    assertThat(game.cities().get(City.Name.ATLANTA).getDiseases().get(Disease.Color.BLUE))
        .isEqualTo(1);
    assertThat(game.cities().get(City.Name.BAGHDAD).getDiseases().get(Disease.Color.BLACK))
        .isEqualTo(1);
  }

  @Test
  public void shouldThrowInvalidActionRequestWhenPassingInvalidAction() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new DrawStepAction();

    assertThrows(
        InvalidActionRequest.class, () -> infectCitiesStep.executeAction(game, actionRequest));
  }
}
