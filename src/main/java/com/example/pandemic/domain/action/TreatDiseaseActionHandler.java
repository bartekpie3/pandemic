package com.example.pandemic.domain.action;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.TreatDiseaseActionRequest;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class TreatDiseaseActionHandler implements ActionHandler<TreatDiseaseActionRequest> {

  private static final int MIN_DISEASE_TO_TREAT = 1;

  @Override
  public Result<String, String> handle(Game game, TreatDiseaseActionRequest action) {
    var activePlayer = game.getActivePlayer();
    var currentLocation = game.cities().get(activePlayer.getCurrentLocation());
    var disease = action.disease();

    var toTreat = howMuchToTreat(game, activePlayer, disease);

    var cured = currentLocation.treatDisease(disease, toTreat);
    activePlayer.takeAction();

    checkIsDiseaseShouldBeEradicated(game, disease);

    log.info(
        "Player {} has been cured {} {} from {}.", activePlayer, cured, disease, currentLocation);

    return new Result.Success<>(String.format("Player cured %d disease", cured));
  }

  private int howMuchToTreat(Game game, Player activePlayer, Disease.Color disease) {
    if (game.isDiseaseCured(disease) || activePlayer.roleIs(Player.Role.MEDIC)) {
      return City.MAX_DISEASE_IN_CITY;
    }

    return MIN_DISEASE_TO_TREAT;
  }

  private void checkIsDiseaseShouldBeEradicated(Game game, Disease.Color disease) {
    if (!game.isDiseaseCured(disease)) {
      return;
    }

    if (game.cities().isDiseaseEradicated(disease)) {
      game.eradicateDisease(disease);

      log.info("Disease {} is eradicated", disease);
    }
  }
}
