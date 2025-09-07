package com.example.pandemic.userinterface.http.action;

import com.example.common.validation.ValidEnum;
import com.example.pandemic.domain.action.move.MoveType;
import com.example.pandemic.domain.model.City;
import jakarta.validation.constraints.NotNull;

public record DispatcherMovePlayerActionRequest(
    @NotNull int playerIndexWhichMoves,
    @ValidEnum(enumClass = City.Name.class) @NotNull String destinationCityName,
    @ValidEnum(enumClass = MoveType.class) @NotNull String moveType) {}
