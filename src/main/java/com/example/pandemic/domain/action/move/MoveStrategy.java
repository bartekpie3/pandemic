package com.example.pandemic.domain.action.move;

import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;

public interface MoveStrategy {

  boolean canMove(Player activePlayer, City currentLocation, City destination);

  default void moveEffect(Player activePlayer, City destination) {}
}
