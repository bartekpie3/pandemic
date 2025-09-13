package com.example.pandemic.domain.step;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.ActionRequest;

public interface GameStep {

  Result<String, String> executeAction(Game game, ActionRequest actionRequest);

  void moveToNextStep(Game game);

  default boolean canMoveToNextStep(Game game) {
    return true;
  }
}
