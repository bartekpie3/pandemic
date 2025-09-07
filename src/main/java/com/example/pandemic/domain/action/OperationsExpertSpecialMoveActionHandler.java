package com.example.pandemic.domain.action;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.OperationsExpertSpecialMoveActionRequest;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class OperationsExpertSpecialMoveActionHandler
    implements ActionHandler<OperationsExpertSpecialMoveActionRequest> {

  @Override
  public Result<String, String> handle(Game game, OperationsExpertSpecialMoveActionRequest action) {
    var activePlayer = game.getActivePlayer();
    var currentLocation = game.cities().get(activePlayer.getCurrentLocation());
    var cardUsed = PlayerCard.tryFromName(action.cardName());

    var validationResult = validate(currentLocation, activePlayer, cardUsed);

    if (!validationResult.isSuccess()) {
      return validationResult;
    }

    activePlayer.setCurrentLocation(action.destinationCityName());
    activePlayer.discardCard(cardUsed);
    activePlayer.useSpecialAction();
    activePlayer.takeAction();

    log.info(
        "Player {} move from {} to {} by operations expert special move",
        activePlayer,
        currentLocation,
        action.destinationCityName());

    return new Result.Success<>("Player moved to " + action.destinationCityName());
  }

  private Result<String, String> validate(
      City currentLocation, Player activePlayer, PlayerCard cardUsed) {
    if (!activePlayer.roleIs(Player.Role.OPERATIONS_EXPERT)) {
      return new Result.Failure<>("Player is not Operations expert");
    }

    if (activePlayer.isHasSpecialActionUsed()) {
      return new Result.Failure<>("Player has already used special action");
    }

    if (!currentLocation.hasResearchStation()) {
      return new Result.Failure<>("Player is not in a research station");
    }

    if (!activePlayer.hasCard(cardUsed)) {
      return new Result.Failure<>("Player has no used card - " + cardUsed);
    }

    return new Result.Success<>(null);
  }
}
