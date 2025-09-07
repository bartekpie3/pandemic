package com.example.pandemic.domain.track;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.pandemic.domain.exception.InvalidMarkerTrackPosition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class InfectionRateTrackTests {

  @Test
  public void shouldMoveMarkerToNextPosition() {
    var track = new InfectionRateTrack(0);

    track.moveMarker();

    assertThat(track.getCurrentValue()).isEqualTo(2);
  }

  @ParameterizedTest
  @CsvSource({"0,2", "1,2", "2,2", "3,3", "4,3", "5,4", "6,4"})
  public void shouldReturnCorrectValue(int initValue, int expectedValue) {
    var track = new InfectionRateTrack(initValue);

    assertThat(track.getCurrentValue()).isEqualTo(expectedValue);
  }

  @Test
  public void shouldThrowExceptionWhenTryMoveMarkerOverLastPosition() {
    var track = new InfectionRateTrack(6);

    assertThrows(InvalidMarkerTrackPosition.class, track::moveMarker);
  }
}
