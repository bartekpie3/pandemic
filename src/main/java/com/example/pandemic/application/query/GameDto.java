package com.example.pandemic.application.query;

import com.example.pandemic.domain.model.Disease;
import java.util.Map;

public record GameDto(
    String id,
    String state,
    int currentPlayerTurnIndex,
    Map<Disease.Color, DiseaseDto> diseases,
    int outbreakMarkerPosition,
    int infectionRatePosition) {}
