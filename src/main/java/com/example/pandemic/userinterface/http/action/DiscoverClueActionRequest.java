package com.example.pandemic.userinterface.http.action;

import com.example.common.validation.ValidEnum;
import com.example.pandemic.domain.model.City;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record DiscoverClueActionRequest(
    @ValidEnum(enumClass = City.Name.class) @NotNull Set<String> cardsUsed) {}
