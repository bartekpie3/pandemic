package com.example.pandemic.domain.step;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.ActionRequest;
import com.example.pandemic.domain.action.request.DrawStepActionRequest;
import com.example.pandemic.domain.card.CardDeck;
import com.example.pandemic.domain.card.DeckHelper;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.exception.InvalidActionRequest;
import com.example.pandemic.domain.service.InfectCityFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class DrawStep implements GameStep {

  @Override
  public Result<String, String> executeAction(Game game, ActionRequest actionRequest) {
    if (!(actionRequest instanceof DrawStepActionRequest)) {
      throw new InvalidActionRequest("Invalid action - required draw step action");
    }

    var playerDeck = game.getPlayerDeck();

    if (playerDeck.isEmpty()) {
      game.lose();

      return new Result.Failure<>("No cards left in player card deck - game over");
    }

    var drawnCard = drawCard(game, playerDeck);

    return new Result.Success<>(
        (game.isFirstDraw() ? 1 : 2) + " draw step executed, " + drawnCard + " drawn card");
  }

  private PlayerCard drawCard(Game game, CardDeck<PlayerCard> playerDeck) {
    var activePlayer = game.getActivePlayer();
    var drawnCard = playerDeck.drawCard();

    log.info("Player {} drawn card {}", activePlayer, drawnCard);

    if (drawnCard.isInfectionCard()) {
      makeInfectionProcess(game);
    } else {
      activePlayer.addCard(drawnCard);
    }

    return drawnCard;
  }

  private void makeInfectionProcess(Game game) {
    game.moveInfectionRateMarker();

    var infectionDeck = game.getInfectionDeck();
    var infectionDiscardPile = game.getInfectionDiscardPile();
    var drawnInfectionCityCard = infectionDeck.drawBottomCard();

    var infector = InfectCityFactory.createInfector();
    infector.infect(game, drawnInfectionCityCard);

    infectionDiscardPile.addCard(drawnInfectionCityCard);

    DeckHelper.shuffleGivenDeckOnTopOfTheDeck(infectionDeck, infectionDiscardPile);
  }

  @Override
  public void moveToNextStep(Game game) {
    game.goToSecondDrawStepOrInfectStep();
  }
}
