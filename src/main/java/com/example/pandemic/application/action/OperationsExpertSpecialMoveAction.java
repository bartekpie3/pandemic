package com.example.pandemic.application.action;

import com.example.pandemic.domain.action.request.OperationsExpertSpecialMoveActionRequest;
import com.example.pandemic.domain.model.City;
import lombok.NonNull;

public record OperationsExpertSpecialMoveAction(
    City.@NonNull  Name destinationCityName, @NonNull String cardName)
    implements OperationsExpertSpecialMoveActionRequest {}
