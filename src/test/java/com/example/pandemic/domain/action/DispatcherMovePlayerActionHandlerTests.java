package com.example.pandemic.domain.action;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.action.DispatcherMovePlayerAction;
import com.example.pandemic.domain.action.move.MoveType;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.support.GameBuilder;
import com.example.pandemic.support.PlayerBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DispatcherMovePlayerActionHandlerTests {

  private DispatcherMovePlayerActionHandler handler = new DispatcherMovePlayerActionHandler();

  @Test
  public void shouldHandleDispatcherMovePlayerAction() {
    var game =
        GameBuilder.aGame()
            .withPlayers(
                List.of(
                    PlayerBuilder.aPlayer().asDispatcher().build(),
                    PlayerBuilder.aPlayer().asMedic().build()))
            .build();
    var actionRequest = new DispatcherMovePlayerAction(1, City.Name.MIAMI, MoveType.DRIVE);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getPlayer(1).getCurrentLocation()).isEqualTo(City.Name.MIAMI);
  }

  @Test
  public void shouldActivePlayerDispatcherShouldNotHaveCardWhenUseDirectFlight() {
    var game =
        GameBuilder.aGame()
            .withPlayers(
                List.of(
                    PlayerBuilder.aPlayer()
                        .asDispatcher()
                        .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.BAGHDAD)))
                        .build(),
                    PlayerBuilder.aPlayer().asMedic().build()))
            .build();
    var actionRequest =
        new DispatcherMovePlayerAction(1, City.Name.BAGHDAD, MoveType.DIRECT_FLIGHT);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getPlayer(1).getCurrentLocation()).isEqualTo(City.Name.BAGHDAD);
    assertThat(game.getActivePlayer().hasCityCard(City.Name.BAGHDAD)).isFalse();
  }

  @Test
  public void shouldFailHandleWhenActivePlayerIsNotDispatcher() {
    var game =
        GameBuilder.aGame()
            .withPlayers(
                List.of(
                    PlayerBuilder.aPlayer().asOperationsExpert().build(),
                    PlayerBuilder.aPlayer().asMedic().build(),
                    PlayerBuilder.aPlayer().asScientist().build()))
            .build();
    var actionRequest = new DispatcherMovePlayerAction(1, City.Name.MIAMI, MoveType.DRIVE);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Only dispatcher can move players");
  }

  @Test
  public void shouldTreatCuredDiseaseFromDestinationCityWhenMoveMedicPlayer() {
    // Given
    var game =
        GameBuilder.aGame()
            .withPlayers(
                List.of(
                    PlayerBuilder.aPlayer().asDispatcher().build(),
                    PlayerBuilder.aPlayer().asMedic().build(),
                    PlayerBuilder.aPlayer().asScientist().build()))
            .build();
    var actionRequest = new DispatcherMovePlayerAction(1, City.Name.MIAMI, MoveType.DRIVE);
    game.cities().get(City.Name.MIAMI).addDisease(City.Name.MIAMI.getColor(), 2);
    game.discoverCure(City.Name.MIAMI.getColor());

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getPlayer(1).getCurrentLocation()).isEqualTo(City.Name.MIAMI);
    assertThat(game.cities().get(City.Name.MIAMI).getDiseases().get(City.Name.MIAMI.getColor()))
        .isEqualTo(0);
    assertThat(game.isDiseaseEradicated(City.Name.MIAMI.getColor())).isTrue();
  }
}
