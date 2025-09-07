package com.example.pandemic.application.action;

import com.example.pandemic.domain.action.request.TreatDiseaseActionRequest;
import com.example.pandemic.domain.model.Disease;
import lombok.NonNull;

public record TreatDiseaseAction(@NonNull Disease.Color disease) implements TreatDiseaseActionRequest {}
