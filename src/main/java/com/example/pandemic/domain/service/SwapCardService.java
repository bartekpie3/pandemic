package com.example.pandemic.domain.service;

import static com.example.pandemic.domain.model.Player.Role.RESEARCHER;

import com.example.common.Result;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;

public final class SwapCardService {

  private SwapCardService() {}

  public static Result<String, String> swap(
      Player firstPlayer, Player secondPlayer, City.Name cityCardName) {
    var validationResult = validatePlayers(firstPlayer, secondPlayer);

    if (!validationResult.isSuccess()) {
      return validationResult;
    }

    if (isResearcherGiveAllowed(firstPlayer, cityCardName)) {
      exchangeCityCard(firstPlayer, secondPlayer, cityCardName);

      return new Result.Success<>(null);
    }

    if (isResearcherGiveAllowed(secondPlayer, cityCardName)) {
      exchangeCityCard(secondPlayer, firstPlayer, cityCardName);

      return new Result.Success<>(null);
    }

    // Standard rule: share the city card that matches the current city.
    if (!isCityAtCurrentLocation(firstPlayer, cityCardName)) {
      return new Result.Failure<>("Card does not match current city");
    }

    if (tryExchangeIfOwner(firstPlayer, secondPlayer, cityCardName)
        || tryExchangeIfOwner(secondPlayer, firstPlayer, cityCardName)) {
      return new Result.Success<>(null);
    }

    return new Result.Failure<>("Player has no required card - " + cityCardName);
  }

  private static Result<String, String> validatePlayers(Player firstPlayer, Player secondPlayer) {
    if (firstPlayer.equals(secondPlayer)) {
      return new Result.Failure<>("Players are the same");
    }

    if (!firstPlayer.getCurrentLocation().equals(secondPlayer.getCurrentLocation())) {
      return new Result.Failure<>("Players are not in the same city");
    }

    return new Result.Success<>(null);
  }

  // Researcher rule: the player can give any city card they possess.
  private static boolean isResearcherGiveAllowed(Player player, City.Name cityCardName) {
    return player.roleIs(RESEARCHER) && player.hasCityCard(cityCardName);
  }

  private static boolean isCityAtCurrentLocation(Player firstPlayer, City.Name cityCardName) {
    return cityCardName.equals(firstPlayer.getCurrentLocation());
  }

  private static boolean tryExchangeIfOwner(
      Player sender, Player receiver, City.Name cityCardName) {
    if (!sender.hasCityCard(cityCardName)) {
      return false;
    }

    exchangeCityCard(sender, receiver, cityCardName);

    return true;
  }

  private static void exchangeCityCard(Player sender, Player receiver, City.Name cityCardName) {
    var card = sender.getCityCard(cityCardName);

    sender.discardCard(card);
    receiver.addCard(card);
  }
}
