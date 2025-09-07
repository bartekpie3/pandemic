package com.example.pandemic.domain.action.move;

import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;

/** Move from a city with a research station to any other city that has a research station. */
final class ShuttleFlightMove implements MoveStrategy {

  @Override
  public boolean canMove(Player activePlayer, City currentLocation, City destination) {
    return currentLocation.hasResearchStation() && destination.hasResearchStation();
  }
}
