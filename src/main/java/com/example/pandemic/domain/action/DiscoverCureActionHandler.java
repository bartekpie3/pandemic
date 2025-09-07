package com.example.pandemic.domain.action;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.DiscoverClueActionRequest;
import com.example.pandemic.domain.collection.Cities;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class DiscoverCureActionHandler implements ActionHandler<DiscoverClueActionRequest> {

  @Override
  public Result<String, String> handle(Game game, DiscoverClueActionRequest action) {
    var activePlayer = game.getActivePlayer();
    var cardsUsed = action.cardsUsed();
    var howManyCardNeeded = activePlayer.roleIs(Player.Role.SCIENTIST) ? 4 : 5;
    var currentLocation = game.cities().get(activePlayer.getCurrentLocation());

    var validationResult = validate(activePlayer, currentLocation, howManyCardNeeded, cardsUsed);

    if (!validationResult.isSuccess()) {
      return validationResult;
    }

    var disease = specifyDisease(cardsUsed);

    game.discoverCure(disease);
    playerDiscardCards(activePlayer, cardsUsed);
    activePlayer.takeAction();

    log.info("Player {} discover cure for {}", game.getActivePlayer(), disease);

    treatDiseaseAtMedicLocation(game, disease);
    checkAndEradicateDisease(game, disease);

    return new Result.Success<>("Player discovered cure");
  }

  private void playerDiscardCards(Player activePlayer, Set<City.Name> cardsUsed) {
    cardsUsed.forEach(
        cityName -> {
          var cityCard = activePlayer.getCityCard(cityName);
          activePlayer.discardCard(cityCard);
        });
  }

  private void treatDiseaseAtMedicLocation(Game game, Disease.Color disease) {
    game.findPlayer(Player.Role.MEDIC)
        .map(
            p -> {
              game.cities().get(p.getCurrentLocation()).treatDisease(disease, City.MAX_DISEASE);
              return p;
            })
        .ifPresent(
            p ->
                log.info(
                    "Disease {} was cured from medic location {}",
                    disease,
                    p.getCurrentLocation()));
  }

  private void checkAndEradicateDisease(Game game, Disease.Color disease) {
    if (game.cities().isDiseaseEradicated(disease)) {
      game.eradicateDisease(disease);

      log.info("Disease {} is eradicated", disease);
    }
  }

  private Disease.Color specifyDisease(Set<City.Name> cardsUsed) {
    return cardsUsed.iterator().next().getColor();
  }

  private Result<String, String> validate(
      Player player, City currentLocation, int howManyCardNeeded, Set<City.Name> cardsUsed) {
    if (!currentLocation.hasResearchStation()) {
      return new Result.Failure<>("City has no research station");
    }

    if (cardsUsed.size() != howManyCardNeeded) {
      return new Result.Failure<>(
          "Invalid number of cards were given - required " + howManyCardNeeded + " cards");
    }

    if (player.getCardsInHand().size() < howManyCardNeeded) {
      return new Result.Failure<>("Player has not enough cards");
    }

    if (!Cities.areCitiesInSameColor(cardsUsed)) {
      return new Result.Failure<>("Invalid cards were given");
    }

    for (var card : cardsUsed) {
      if (!player.hasCityCard(card)) {
        return new Result.Failure<>("Player has no required card - " + card);
      }
    }

    return new Result.Success<>(null);
  }
}
