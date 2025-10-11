package com.example.pandemic.domain.action;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.move.MoveStrategyProvider;
import com.example.pandemic.domain.action.request.DispatcherMovePlayerActionRequest;
import com.example.pandemic.domain.model.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class DispatcherMovePlayerActionHandler
    implements ActionHandler<DispatcherMovePlayerActionRequest> {

  @Override
  public Result<String, String> handle(Game game, DispatcherMovePlayerActionRequest action) {
    var activePlayer = game.getActivePlayer();

    if (!activePlayer.roleIs(Player.Role.DISPATCHER)) {
      return new Result.Failure<>("Only dispatcher can move players");
    }

    var playerWhichMoves = game.getPlayer(action.playerIndexWhichMoves());
    var moveStrategy = MoveStrategyProvider.getMoveStrategy(action.moveType());
    var destinationCity = game.cities().get(action.destinationCityName());
    var currentLocation = game.cities().get(playerWhichMoves.getCurrentLocation());

    if (!moveStrategy.canMove(activePlayer, currentLocation, destinationCity)) {
      return new Result.Failure<>(
          "Player can not move to this city with this move type: " + action.moveType());
    }

    moveStrategy.moveEffect(activePlayer, destinationCity);
    playerWhichMoves.setCurrentLocation(destinationCity.getName());
    activePlayer.takeAction();

    // Rule: Medic automatically removes all cubes of a cured disease from their city
    MedicMoveResolver.medicClearCuredDiseaseOnEnter(playerWhichMoves, game);

    log.info(
        "Dispatcher move player {} from {} to {}",
        playerWhichMoves,
        currentLocation,
        playerWhichMoves.getCurrentLocation());

    return new Result.Success<>("Player moved");
  }
}
