package com.example.pandemic.application.action;

import com.example.pandemic.domain.action.move.MoveType;
import com.example.pandemic.domain.action.request.DispatcherMovePlayerActionRequest;
import com.example.pandemic.domain.model.City;
import lombok.NonNull;

public record DispatcherMovePlayerAction(
    int playerIndexWhichMoves, City.@NonNull Name destinationCityName, @NonNull MoveType moveType)
    implements DispatcherMovePlayerActionRequest {}
