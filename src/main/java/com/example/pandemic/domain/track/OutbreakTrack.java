package com.example.pandemic.domain.track;

import com.example.pandemic.domain.exception.InvalidMarkerTrackPosition;
import lombok.Getter;

public final class OutbreakTrack {

  private static final int LAST_TRACK_POSITION = 8;

  @Getter private int markerPosition;

  public OutbreakTrack(int markerPosition) {
    if (markerPosition < 0 || markerPosition > LAST_TRACK_POSITION) {
      throw InvalidMarkerTrackPosition.invalidInitPosition();
    }

    this.markerPosition = markerPosition;
  }

  public static OutbreakTrack init() {
    return new OutbreakTrack(0);
  }

  public void moveMarker() {
    if (isOnLastPosition()) {
      throw InvalidMarkerTrackPosition.outOfRangePosition();
    }

    markerPosition++;
  }

  public boolean isOnLastPosition() {
    return markerPosition == LAST_TRACK_POSITION;
  }
}
