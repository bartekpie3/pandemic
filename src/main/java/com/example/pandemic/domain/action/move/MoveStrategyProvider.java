package com.example.pandemic.domain.action.move;

public final class MoveStrategyProvider {

  public static MoveStrategy getMoveStrategy(MoveType moveType) {
    return switch (moveType) {
      case MoveType.DRIVE -> new DriveMove();
      case MoveType.CHARTER_FLIGHT -> new CharterFlightMove();
      case MoveType.DIRECT_FLIGHT -> new DirectFlightMove();
      case MoveType.SHUTTLE_FLIGHT -> new ShuttleFlightMove();
    };
  }
}
