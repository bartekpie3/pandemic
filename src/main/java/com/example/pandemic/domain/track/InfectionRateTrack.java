package com.example.pandemic.domain.track;

import com.example.pandemic.domain.exception.InvalidMarkerTrackPosition;
import lombok.Getter;

public final class InfectionRateTrack {

  private static final int LAST_TRACK_POSITION = 6;

  private static final Integer[] VALUE_MAP = {2, 2, 2, 3, 3, 4, 4};

  @Getter private int markerPosition;

  public InfectionRateTrack(int markerPosition) {
    if (markerPosition < 0 || markerPosition > LAST_TRACK_POSITION) {
      throw InvalidMarkerTrackPosition.invalidInitPosition();
    }

    this.markerPosition = markerPosition;
  }

  public static InfectionRateTrack init() {
    return new InfectionRateTrack(0);
  }

  public void moveMarker() {
    if (isOnLastPosition()) {
      throw InvalidMarkerTrackPosition.outOfRangePosition();
    }

    markerPosition++;
  }

  public int getCurrentValue() {
    return VALUE_MAP[markerPosition];
  }

  private boolean isOnLastPosition() {
    return markerPosition == LAST_TRACK_POSITION;
  }
}
