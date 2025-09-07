package com.example.pandemic.domain.action.move;

import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;

/** Discard a City card to move to the city named on the card */
final class DirectFlightMove implements MoveStrategy {

  @Override
  public boolean canMove(Player activePlayer, City currentLocation, City destination) {
    return activePlayer.hasCityCard(destination.getName());
  }

  @Override
  public void moveEffect(Player activePlayer, City destination) {
    var card = activePlayer.getCityCard(destination.getName());

    activePlayer.discardCard(card);
  }
}
