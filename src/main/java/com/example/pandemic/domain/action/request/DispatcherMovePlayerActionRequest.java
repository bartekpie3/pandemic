package com.example.pandemic.domain.action.request;

import com.example.pandemic.domain.action.move.MoveType;
import com.example.pandemic.domain.model.City;
import lombok.NonNull;

public interface DispatcherMovePlayerActionRequest extends PlayerActionRequest {

  @NonNull
  int playerIndexWhichMoves();

  @NonNull
  City.Name destinationCityName();

  @NonNull
  MoveType moveType();
}
