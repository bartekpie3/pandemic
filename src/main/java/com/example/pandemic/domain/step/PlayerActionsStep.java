package com.example.pandemic.domain.step;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.PlayerActionHandlerProvider;
import com.example.pandemic.domain.action.request.ActionRequest;
import com.example.pandemic.domain.action.request.PlayerActionRequest;
import com.example.pandemic.domain.exception.InvalidActionRequest;
import com.example.pandemic.domain.exception.PlayerHasNoMoreAvailableActions;

final class PlayerActionsStep implements GameStep {

  @Override
  public Result<String, String> executeAction(Game game, ActionRequest actionRequest) {
    if (!(actionRequest instanceof PlayerActionRequest)) {
      throw new InvalidActionRequest("Invalid action - required player action");
    }

    if (!game.getActivePlayer().hasAvailableActions()) {
      throw new PlayerHasNoMoreAvailableActions("Player has no more available actions");
    }

    var actionHandler = PlayerActionHandlerProvider.getActionHandler(actionRequest);

    return actionHandler.handle(game, actionRequest);
  }

  @Override
  public void moveToNextStep(Game game) {
    game.goToDrawState();
  }

  @Override
  public boolean canMoveToNextStep(Game game) {
    return !game.getActivePlayer().hasAvailableActions();
  }
}
