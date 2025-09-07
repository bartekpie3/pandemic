package com.example.pandemic.domain.action.move;

import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;
import com.example.pandemic.domain.service.CityConnections;

/** Move to a city connected by a white line to the one you are in */
final class DriveMove implements MoveStrategy {

  @Override
  public boolean canMove(Player activePlayer, City currentLocation, City destination) {
    return CityConnections.hasConnection(currentLocation.getName(), destination.getName());
  }
}
