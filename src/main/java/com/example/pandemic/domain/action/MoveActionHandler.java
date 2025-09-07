package com.example.pandemic.domain.action;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.move.MoveStrategyProvider;
import com.example.pandemic.domain.action.request.MoveActionRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class MoveActionHandler implements ActionHandler<MoveActionRequest> {

  @Override
  public Result<String, String> handle(Game game, MoveActionRequest action) {
    var activePlayer = game.getActivePlayer();
    var currentLocation = game.cities().get(activePlayer.getCurrentLocation());
    var moveStrategy = MoveStrategyProvider.getMoveStrategy(action.moveType());
    var desinationCity = game.cities().get(action.destinationCityName());

    if (!moveStrategy.canMove(activePlayer, currentLocation, desinationCity)) {
      return new Result.Failure<>(
          "Player can not move to this city with this move type: " + action.moveType());
    }

    moveStrategy.moveEffect(activePlayer, desinationCity);
    activePlayer.setCurrentLocation(desinationCity.getName());
    activePlayer.takeAction();

    MedicMoveResolver.medicClearCuredDiseaseOnEnter(activePlayer, game);

    log.info(
        "Player {} move from {} to {} by {} move type",
        activePlayer,
        currentLocation,
        action.destinationCityName(),
        action.moveType());

    return new Result.Success<>(String.format("Player moved to %s", desinationCity.getName()));
  }
}
