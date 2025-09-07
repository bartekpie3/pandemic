package com.example.pandemic.domain.step;

import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.exception.InvalidGameState;

public final class GameStepProvider {

  private GameStepProvider() {}

  public static GameStep determineGameStep(Game.State state) {
    return switch (state) {
      case ACTION -> new PlayerActionsStep();
      case FIRST_DRAW, SECOND_DRAW -> new DrawStep();
      case INFECT -> new InfectCitiesStep();
      default -> throw new InvalidGameState("Invalid game state - " + state);
    };
  }
}
