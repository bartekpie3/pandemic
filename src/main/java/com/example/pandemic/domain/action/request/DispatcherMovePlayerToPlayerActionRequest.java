package com.example.pandemic.domain.action.request;

import lombok.NonNull;

public interface DispatcherMovePlayerToPlayerActionRequest extends PlayerActionRequest {

    @NonNull int playerIndexWhichMoves();

    @NonNull int playerIndexToWhichMoves();
}
