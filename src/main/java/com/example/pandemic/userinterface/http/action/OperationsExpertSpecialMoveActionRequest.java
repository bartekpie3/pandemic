package com.example.pandemic.userinterface.http.action;

import com.example.common.validation.ValidEnum;
import com.example.pandemic.domain.model.City;
import jakarta.validation.constraints.NotNull;

public record OperationsExpertSpecialMoveActionRequest(
    @ValidEnum(enumClass = City.Name.class) @NotNull String destinationCityName,
    @NotNull String cardName) {}
