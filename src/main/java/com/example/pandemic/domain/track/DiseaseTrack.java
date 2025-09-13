package com.example.pandemic.domain.track;

import static com.example.pandemic.domain.model.Disease.Color.*;

import com.example.pandemic.domain.exception.DiseaseIsNotCured;
import com.example.pandemic.domain.model.Disease;
import java.util.EnumMap;
import java.util.Map;

public record DiseaseTrack(Map<Disease.Color, Disease> diseases) {

  public static DiseaseTrack init() {
    EnumMap<Disease.Color, Disease> diseases = new EnumMap<>(Disease.Color.class);
    diseases.put(BLACK, new Disease(BLACK));
    diseases.put(RED, new Disease(RED));
    diseases.put(YELLOW, new Disease(YELLOW));
    diseases.put(BLUE, new Disease(BLUE));

    return new DiseaseTrack(diseases);
  }

  public void cureDisease(Disease.Color diseaseColor) {
    var disease = diseases.get(diseaseColor);

    disease.cure();

    diseases.put(diseaseColor, disease);
  }

  public void eradicateDisease(Disease.Color diseaseColor) {
    var disease = diseases.get(diseaseColor);

    if (!disease.isCured()) {
      throw new DiseaseIsNotCured("Disease is not cured");
    }

    disease.eradicate();

    diseases.put(diseaseColor, disease);
  }

  public boolean isDiseaseCured(Disease.Color disease) {
    return diseases.get(disease).isCured();
  }

  public boolean isDiseaseEradicated(Disease.Color disease) {
    return diseases.get(disease).isEradicated();
  }

  public boolean areAllDiseasesCured() {
    return diseases.values().stream().allMatch(Disease::isCured);
  }
}
