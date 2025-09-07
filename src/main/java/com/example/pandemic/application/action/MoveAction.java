package com.example.pandemic.application.action;

import com.example.pandemic.domain.action.request.MoveActionRequest;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.action.move.MoveType;
import lombok.NonNull;

public record MoveAction(City.@NonNull Name destinationCityName, @NonNull MoveType moveType)
    implements MoveActionRequest {
}
