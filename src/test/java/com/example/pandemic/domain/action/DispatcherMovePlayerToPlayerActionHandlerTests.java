package com.example.pandemic.domain.action;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.action.DispatcherMovePlayerToPlayerAction;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.support.GameBuilder;
import com.example.pandemic.support.PlayerBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DispatcherMovePlayerToPlayerActionHandlerTests {

  private final DispatcherMovePlayerToPlayerActionHandler handler = new DispatcherMovePlayerToPlayerActionHandler();

  @Test
  public void shouldHandleDispatcherMovePlayerAction() {
    var game =
        GameBuilder.aGame()
            .withPlayers(
                List.of(
                    PlayerBuilder.aPlayer().asDispatcher().build(),
                    PlayerBuilder.aPlayer().asMedic().build(),
                    PlayerBuilder.aPlayer().asScientist().inCity(City.Name.BAGHDAD).build()))
            .build();
    var actionRequest = new DispatcherMovePlayerToPlayerAction(1, 2);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getPlayer(1).getCurrentLocation()).isEqualTo(City.Name.BAGHDAD);
  }

  @Test
  public void shouldFailHandleWhenActivePlayerIsNotDispatcher() {
    var game =
        GameBuilder.aGame()
            .withPlayers(
                List.of(
                    PlayerBuilder.aPlayer().asResearcher().build(),
                    PlayerBuilder.aPlayer().asMedic().build(),
                    PlayerBuilder.aPlayer().asScientist().inCity(City.Name.BAGHDAD).build()))
            .build();
    var actionRequest = new DispatcherMovePlayerToPlayerAction(1, 2);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Only dispatcher can move players");
  }

  @Test
  public void shouldFailHandleWhenGivenPlayersAreTheSame() {
    var game =
        GameBuilder.aGame()
            .withPlayers(
                List.of(
                    PlayerBuilder.aPlayer().asDispatcher().build(),
                    PlayerBuilder.aPlayer().asMedic().build(),
                    PlayerBuilder.aPlayer().asScientist().inCity(City.Name.BAGHDAD).build()))
            .build();
    var actionRequest = new DispatcherMovePlayerToPlayerAction(1, 1);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Player can not move to himself");
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
                    PlayerBuilder.aPlayer().asScientist().inCity(City.Name.BAGHDAD).build()))
            .build();
    var actionRequest = new DispatcherMovePlayerToPlayerAction(1, 2);
    game.cities().get(City.Name.BAGHDAD).addDisease(City.Name.BAGHDAD.getColor(), 2);
    game.discoverCure(City.Name.BAGHDAD.getColor());

    // When
    var result = handler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getPlayer(1).getCurrentLocation()).isEqualTo(City.Name.BAGHDAD);
    assertThat(game.cities().get(City.Name.BAGHDAD).getDiseases().get(City.Name.BAGHDAD.getColor()))
        .isEqualTo(0);
    assertThat(game.isDiseaseEradicated(City.Name.BAGHDAD.getColor())).isTrue();
  }
}
