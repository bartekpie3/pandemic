package com.example.pandemic.domain.track;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.pandemic.domain.exception.InvalidMarkerTrackPosition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class OutbreakTrackTests {

  @Test
  public void shouldMoveMarkerToNextPosition() {
    var track = new OutbreakTrack(0);

    track.moveMarker();

    assertThat(track.getMarkerPosition()).isEqualTo(1);
    assertThat(track.isOnLastPosition()).isFalse();
  }

  @Test
  public void shouldThrowExceptionWhenTryMoveMarkerOverLastPosition() {
    var track = new OutbreakTrack(7);

    track.moveMarker();

    assertThrows(InvalidMarkerTrackPosition.class, track::moveMarker);
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 10}) // six numbers
  public void shouldThrowExceptionWhenCreateTrackWithInvalidInitialPosition(int initialPosition) {
    assertThrows(InvalidMarkerTrackPosition.class, () -> new OutbreakTrack(initialPosition));
  }
}
