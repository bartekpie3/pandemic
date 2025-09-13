package com.example.pandemic.domain.step;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.ActionRequest;
import com.example.pandemic.domain.action.request.InfectStepActionRequest;
import com.example.pandemic.domain.card.CardDeck;
import com.example.pandemic.domain.card.InfectionCard;
import com.example.pandemic.domain.exception.InvalidActionRequest;
import com.example.pandemic.domain.service.InfectCityFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class InfectCitiesStep implements GameStep {

  @Override
  public Result<String, String> executeAction(Game game, ActionRequest actionRequest) {
    if (!(actionRequest instanceof InfectStepActionRequest)) {
      throw new InvalidActionRequest("Invalid action - required infect step action");
    }

    var infectionDeck = game.getInfectionDeck();
    var infectionDiscardPile = game.getInfectionDiscardPile();

    infectCities(game, infectionDeck, infectionDiscardPile);

    log.info("Infected cities step executed");

    return new Result.Success<>("Infected cities step executed");
  }

  private void infectCities(
      Game game,
      CardDeck<InfectionCard> infectionDeck,
      CardDeck<InfectionCard> infectionDiscardPile) {
    var citesToInfect = game.getInfectionRate();
    var infector = InfectCityFactory.createInfector();

    for (var i = 0; i < citesToInfect; i++) {
      var infectionCard = infectionDeck.drawCard();

      infector.infect(game, infectionCard);

      infectionDiscardPile.addCard(infectionCard);
    }
  }

  @Override
  public void moveToNextStep(Game game) {
    game.goToActionState();
  }
}
