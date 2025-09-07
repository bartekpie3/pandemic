package com.example.pandemic.application.action;

import com.example.pandemic.domain.action.request.DispatcherMovePlayerToPlayerActionRequest;

public record DispatcherMovePlayerToPlayerAction(int playerIndexWhichMoves, int playerIndexToWhichMoves)
    implements DispatcherMovePlayerToPlayerActionRequest {}
