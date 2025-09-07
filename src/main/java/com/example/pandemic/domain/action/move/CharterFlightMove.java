package com.example.pandemic.domain.action.move;

import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;
import lombok.NonNull;

/** Discard the City card that matches the city you are in to move to any city */
final class CharterFlightMove implements MoveStrategy {

  @Override
  public boolean canMove(Player activePlayer, City currentLocation, City destination) {
    return activePlayer.hasCityCard(activePlayer.getCurrentLocation());
  }

  @Override
  public void moveEffect(Player activePlayer, City destination) {
    var card = activePlayer.getCityCard(activePlayer.getCurrentLocation());

    activePlayer.discardCard(card);
  }
}
