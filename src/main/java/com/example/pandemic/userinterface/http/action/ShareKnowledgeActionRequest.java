package com.example.pandemic.userinterface.http.action;

import com.example.common.validation.ValidEnum;
import com.example.pandemic.domain.model.City;
import jakarta.validation.constraints.NotNull;

public record ShareKnowledgeActionRequest(
    @NotNull int playerToSwap,
    @ValidEnum(enumClass = City.Name.class) @NotNull String cityCardName) {}
