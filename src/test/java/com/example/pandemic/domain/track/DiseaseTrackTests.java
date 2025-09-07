package com.example.pandemic.domain.track;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.domain.model.Disease;
import org.junit.jupiter.api.Test;

public class DiseaseTrackTests {

  @Test
  public void shouldDiseaseNotBeCuredWhenCheckOnNotCurredDisease() {
    var track = DiseaseTrack.init();

    var isDiseaseCured = track.isDiseaseCured(Disease.Color.RED);

    assertThat(isDiseaseCured).isFalse();
  }

  @Test
  public void shouldDiseaseNotBeEradicatedWhenCheckOnNotEradicatedDisease() {
    var track = DiseaseTrack.init();

    var isDiseaseEradicated = track.isDiseaseEradicated(Disease.Color.RED);

    assertThat(isDiseaseEradicated).isFalse();
  }

  @Test
  public void shouldCureDiseaseWithSuccess() {
    var track = DiseaseTrack.init();
    var disease = Disease.Color.RED;

    track.cureDisease(disease);

    var isDiseaseCured = track.isDiseaseCured(disease);

    assertThat(isDiseaseCured).isTrue();
  }

  @Test
  public void shouldReturnTrueWhenAllDiseaseAreCured() {
    var track = DiseaseTrack.init();

    track.cureDisease(Disease.Color.RED);
    track.cureDisease(Disease.Color.BLACK);
    track.cureDisease(Disease.Color.BLUE);
    track.cureDisease(Disease.Color.YELLOW);

    var areAllDiseaseCured = track.areAllDiseaseCured();

    assertThat(areAllDiseaseCured).isTrue();
  }

  @Test
  public void shouldReturnFalseWhenNoAllDiseaseAreCured() {
    var track = DiseaseTrack.init();

    track.cureDisease(Disease.Color.RED);

    var areAllDiseaseCured = track.areAllDiseaseCured();

    assertThat(areAllDiseaseCured).isFalse();
  }
}
