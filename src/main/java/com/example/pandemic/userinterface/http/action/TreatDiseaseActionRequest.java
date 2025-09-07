package com.example.pandemic.userinterface.http.action;

import com.example.common.validation.ValidEnum;
import com.example.pandemic.domain.model.Disease;
import jakarta.validation.constraints.NotNull;

public record TreatDiseaseActionRequest(
    @ValidEnum(enumClass = Disease.Color.class) @NotNull String disease) {}
