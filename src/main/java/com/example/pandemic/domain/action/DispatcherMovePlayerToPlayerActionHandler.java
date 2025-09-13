package com.example.pandemic.domain.action;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.DispatcherMovePlayerToPlayerActionRequest;
import com.example.pandemic.domain.model.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class DispatcherMovePlayerToPlayerActionHandler
    implements ActionHandler<DispatcherMovePlayerToPlayerActionRequest> {

  @Override
  public Result<String, String> handle(Game game, DispatcherMovePlayerToPlayerActionRequest action) {
    var activePlayer = game.getActivePlayer();

    if (!activePlayer.roleIs(Player.Role.DISPATCHER)) {
      return new Result.Failure<>("Only dispatcher can move players");
    }

    var playerWhichMoves = game.getPlayer(action.playerIndexWhichMoves());
    var playerWhichMovesPreviousLocation = playerWhichMoves.getCurrentLocation();
    var playerToWhichMoves = game.getPlayer(action.playerIndexToWhichMoves());

    if (playerWhichMoves.equals(playerToWhichMoves)) {
      return new Result.Failure<>("Player can not move to himself");
    }

    playerWhichMoves.setCurrentLocation(playerToWhichMoves.getCurrentLocation());
    activePlayer.takeAction();

    // Rule: Medic automatically removes all cubes of a cured disease from their city
    MedicMoveResolver.medicClearCuredDiseaseOnEnter(playerWhichMoves, game);

    log.info(
        "Dispatcher move player {} from {} to {}",
        playerWhichMoves,
        playerWhichMovesPreviousLocation,
        playerWhichMoves.getCurrentLocation());

    return new Result.Success<>("Player moved");
  }
}
