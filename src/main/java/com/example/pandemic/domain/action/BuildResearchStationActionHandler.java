package com.example.pandemic.domain.action;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.BuildResearchStationActionRequest;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class BuildResearchStationActionHandler
    implements ActionHandler<BuildResearchStationActionRequest> {

  @Override
  public Result<String, String> handle(Game game, BuildResearchStationActionRequest action) {
    var activePlayer = game.getActivePlayer();
    var currentLocation = game.cities().get(activePlayer.getCurrentLocation());

    var validationResult = validate(activePlayer, currentLocation);

    if (!validationResult.isSuccess()) {
      return validationResult;
    }

    discardRequiredCardIfNeeded(activePlayer);
    currentLocation.buildResearchStation();
    activePlayer.takeAction();

    log.info("Player {} built research station in {}", activePlayer, currentLocation);

    return new Result.Success<>("Player built research station in " + currentLocation.getName());
  }

  private void discardRequiredCardIfNeeded(Player activePlayer) {
    if (!activePlayer.roleIs(Player.Role.OPERATIONS_EXPERT)) {
      var cityCard = activePlayer.getCityCard(activePlayer.getCurrentLocation());

      activePlayer.discardCard(cityCard);
    }
  }

  private Result<String, String> validate(Player activePlayer, City currentLocation) {
    if (!activePlayer.hasCityCard(currentLocation.getName())) {
      return new Result.Failure<>("Player has no required card - " + currentLocation.getName());
    }

    if (currentLocation.hasResearchStation()) {
      return new Result.Failure<>("City already has research station");
    }

    return new Result.Success<>(null);
  }
}
