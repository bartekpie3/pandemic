package com.example.pandemic.domain.step;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.pandemic.application.action.DrawStepAction;
import com.example.pandemic.application.action.PassAction;
import com.example.pandemic.domain.exception.InvalidActionRequest;
import com.example.pandemic.domain.exception.PlayerHasNoMoreAvailableActions;
import com.example.pandemic.support.GameBuilder;
import org.junit.jupiter.api.Test;

public class PlayerActionsStepTests {

  private final PlayerActionsStep playerActionsStep = new PlayerActionsStep();

  @Test
  public void shouldThrowInvalidActionRequestWhenActionIsNotPlayerAction() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new DrawStepAction();

    assertThrows(
        InvalidActionRequest.class, () -> playerActionsStep.executeAction(game, actionRequest));
  }

  @Test
  public void shouldThrowPlayerHasNoMoreAvailableActionsWhenPlayerHasNoMoreActions() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new PassAction();

    game.getActivePlayer().pass();

    assertThrows(
        PlayerHasNoMoreAvailableActions.class,
        () -> playerActionsStep.executeAction(game, actionRequest));
  }
}
