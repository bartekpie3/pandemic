package com.example.pandemic.domain.action.request;

import com.example.pandemic.domain.model.City;
import lombok.NonNull;

import java.util.Set;

public interface DiscoverClueActionRequest extends PlayerActionRequest {

  @NonNull Set<City.Name> cardsUsed();
}
