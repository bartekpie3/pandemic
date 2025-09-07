package com.example.pandemic.domain.action.request;

import com.example.pandemic.domain.model.City;
import lombok.NonNull;

public interface OperationsExpertSpecialMoveActionRequest extends PlayerActionRequest {

  @NonNull
  City.Name destinationCityName();

  @NonNull
  String cardName();
}
