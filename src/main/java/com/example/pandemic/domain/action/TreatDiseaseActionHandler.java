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

  @Override
  public Result<String, String> handle(Game game, TreatDiseaseActionRequest action) {
    var activePlayer = game.getActivePlayer();
    var currentLocation = game.cities().get(activePlayer.getCurrentLocation());
    var wasDiseaseCured = game.isDiseaseCured(action.disease());
    var toTreat = wasDiseaseCured || activePlayer.roleIs(Player.Role.MEDIC) ? City.MAX_DISEASE : 1;

    var cured = currentLocation.treatDisease(action.disease(), toTreat);
    activePlayer.takeAction();
    checkIsDiseaseShouldBeEradicated(game, action.disease());

    log.info(
        "Player {} has been cured {} {} from {}.",
        activePlayer,
        cured,
        action.disease(),
        currentLocation);

    return new Result.Success<>(String.format("Player cured %d disease", cured));
  }

  private void checkIsDiseaseShouldBeEradicated(Game game, Disease.Color disease) {
    if (game.isDiseaseCured(disease) && game.cities().isDiseaseEradicated(disease)) {
      game.eradicateDisease(disease);

      log.info("Disease {} is eradicated", disease);
    }
  }
}
