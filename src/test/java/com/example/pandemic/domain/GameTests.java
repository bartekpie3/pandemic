package com.example.pandemic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.pandemic.application.action.DrawStepAction;
import com.example.pandemic.application.action.InfectStepAction;
import com.example.pandemic.application.action.MoveAction;
import com.example.pandemic.application.action.PassAction;
import com.example.pandemic.domain.action.move.MoveType;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.exception.InvalidGameState;
import com.example.pandemic.domain.exception.InvalidPlayer;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.support.GameBuilder;
import com.example.pandemic.support.PlayerBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class GameTests {

  @Test
  public void shouldPerformActionStepWithSuccessAndStayInActionStateWhenPlayerHasMoreActions() {
    var game = GameBuilder.aGame().build();

    var result = game.performAction(new MoveAction(City.Name.MIAMI, MoveType.DRIVE));

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getState()).isEqualTo(Game.State.ACTION.name());
  }

  @Test
  public void shouldFailPerformActionWhenAnyPlayerHasMoreThanSevenCardsInHandInActionState() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(
                        List.of(
                            PlayerCard.createCityCard(City.Name.ATLANTA),
                            PlayerCard.createCityCard(City.Name.BAGHDAD),
                            PlayerCard.createCityCard(City.Name.WASHINGTON),
                            PlayerCard.createCityCard(City.Name.MIAMI),
                            PlayerCard.createCityCard(City.Name.MADRID),
                            PlayerCard.createCityCard(City.Name.SAN_FRANCISCO),
                            PlayerCard.createCityCard(City.Name.BEIJING),
                            PlayerCard.createCityCard(City.Name.OSAKA)))
                    .build())
            .build();

    var result = game.performAction(new MoveAction(City.Name.MIAMI, MoveType.DRIVE));

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Too many cards in player hand");
  }

  @Test
  public void
      shouldPerformActionStepWithSuccessAndMoveToFirstDrawStateWhenPlayerHasNoMoreActions() {
    var game = GameBuilder.aGame().build();

    var result = game.performAction(new PassAction());

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getState()).isEqualTo(Game.State.FIRST_DRAW.name());
  }

  @Test
  public void shouldPerformFirstDrawStepWithSuccess() {
    var game = GameBuilder.aGame().build();
    game.goToDrawState();

    var result = game.performAction(new DrawStepAction());

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getState()).isEqualTo(Game.State.SECOND_DRAW.name());
  }

  @Test
  public void shouldFailPerformActionWhenAnyPlayerHasMoreThanSevenCardsInHandInFirstDrawState() {
    var game =
        GameBuilder.aGame()
            .withPlayers(
                List.of(
                    PlayerBuilder.aPlayer().build(),
                    PlayerBuilder.aPlayer()
                        .asOperationsExpert()
                        .withCardsInHand(
                            List.of(
                                PlayerCard.createCityCard(City.Name.ATLANTA),
                                PlayerCard.createCityCard(City.Name.BAGHDAD),
                                PlayerCard.createCityCard(City.Name.WASHINGTON),
                                PlayerCard.createCityCard(City.Name.MIAMI),
                                PlayerCard.createCityCard(City.Name.MADRID),
                                PlayerCard.createCityCard(City.Name.SAN_FRANCISCO),
                                PlayerCard.createCityCard(City.Name.BEIJING),
                                PlayerCard.createCityCard(City.Name.OSAKA)))
                        .build()))
            .build();
    game.goToDrawState();

    var result = game.performAction(new DrawStepAction());

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Too many cards in player hand");
  }

  @Test
  public void shouldPerformSecondDrawStepWithSuccess() {
    var game = GameBuilder.aGame().build();
    game.goToDrawState();
    game.goToSecondDrawStepOrInfectStep();

    var result = game.performAction(new DrawStepAction());

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getState()).isEqualTo(Game.State.INFECT.name());
  }

  @Test
  public void shouldPerformSecondDrawStepWithSuccessEvenPlayerHasMoreThanSevenCardsInHand() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(
                        List.of(
                            PlayerCard.createCityCard(City.Name.ATLANTA),
                            PlayerCard.createCityCard(City.Name.BAGHDAD),
                            PlayerCard.createCityCard(City.Name.WASHINGTON),
                            PlayerCard.createCityCard(City.Name.MIAMI),
                            PlayerCard.createCityCard(City.Name.MADRID),
                            PlayerCard.createCityCard(City.Name.SAN_FRANCISCO),
                            PlayerCard.createCityCard(City.Name.BEIJING),
                            PlayerCard.createCityCard(City.Name.OSAKA)))
                    .build())
            .build();
    game.goToDrawState();
    game.goToSecondDrawStepOrInfectStep();

    var result = game.performAction(new DrawStepAction());

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getState()).isEqualTo(Game.State.INFECT.name());
  }

  @Test
  public void shouldPerformInfectStepWithSuccess() {
    var game = GameBuilder.aGame().build();
    game.goToSecondDrawStepOrInfectStep();

    var result = game.performAction(new InfectStepAction());

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getState()).isEqualTo(Game.State.ACTION.name());
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(4);
    assertThat(game.getCurrentPlayerTurnIndex()).isEqualTo(1);
  }

  @Test
  public void shouldFailPerformActionWhenAnyPlayerHasMoreThanSevenCardsInHandInInfectState() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(
                        List.of(
                            PlayerCard.createCityCard(City.Name.ATLANTA),
                            PlayerCard.createCityCard(City.Name.BAGHDAD),
                            PlayerCard.createCityCard(City.Name.WASHINGTON),
                            PlayerCard.createCityCard(City.Name.MIAMI),
                            PlayerCard.createCityCard(City.Name.MADRID),
                            PlayerCard.createCityCard(City.Name.SAN_FRANCISCO),
                            PlayerCard.createCityCard(City.Name.BEIJING),
                            PlayerCard.createCityCard(City.Name.OSAKA)))
                    .build())
            .build();
    game.goToSecondDrawStepOrInfectStep();

    var result = game.performAction(new InfectStepAction());

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Too many cards in player hand");
  }

  @Test
  public void shouldGoToFirstPlayerTurnWhenLastPlayerTurnEnd() {
    var game = GameBuilder.aGame().build();
    game.goToActionState();
    game.goToActionState();

    assertThat(game.getCurrentPlayerTurnIndex()).isEqualTo(0);
  }

  @Test
  public void shouldLoseGameWhenOutbreakMarkerIsMovedToLastPosition() {
    var game = GameBuilder.aGame().withOutbreakMarkerPosition(7).build();
    game.moveOutbreakTrack();

    assertThat(game.getState()).isEqualTo(Game.State.LOSE.name());
  }

  @Test
  public void shouldWinGameWhenAllCuresAreDiscovered() {
    var game = GameBuilder.aGame().build();

    game.discoverCure(Disease.Color.BLACK);
    game.discoverCure(Disease.Color.BLUE);
    game.discoverCure(Disease.Color.RED);
    game.discoverCure(Disease.Color.YELLOW);

    assertThat(game.getState()).isEqualTo(Game.State.WIN.name());
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 4})
  public void shouldThrowInvalidPlayerWhenGetPlayerWithIncorrectIndex(int index) {
    var game = GameBuilder.aGame().build();

    assertThrows(InvalidPlayer.class, () -> game.getPlayer(index));
  }

  @Test
  public void shouldThrowInvalidGameStateWhenGameIsInLostState() {
    var game = GameBuilder.aGame().build();
    game.lose();

    assertThrows(InvalidGameState.class, () -> game.performAction(new DrawStepAction()));
  }
}
