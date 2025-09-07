package com.example.pandemic.application.action;

import com.example.pandemic.domain.action.request.DiscoverClueActionRequest;
import com.example.pandemic.domain.model.City;
import lombok.NonNull;

import java.util.Set;

public record DiscoverClueAction(@NonNull Set<City.Name> cardsUsed) implements DiscoverClueActionRequest {}
