package com.example.pandemic.userinterface.http.action;

import jakarta.validation.constraints.NotNull;

public record DispatcherMovePlayerToPlayerActionRequest(
    @NotNull int playerIndexWhichMoves, @NotNull int playerIndexToWhichMoves) {}
