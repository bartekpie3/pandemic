package com.example.pandemic.domain.action.request;

import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.action.move.MoveType;
import lombok.NonNull;

public interface MoveActionRequest extends PlayerActionRequest {

  City.@NonNull Name destinationCityName();

  @NonNull
  MoveType moveType();
}
