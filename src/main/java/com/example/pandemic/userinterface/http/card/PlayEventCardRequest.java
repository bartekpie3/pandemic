package com.example.pandemic.userinterface.http.card;

import com.example.common.validation.ValidEnum;
import com.example.pandemic.domain.card.PlayerCardEventName;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PlayEventCardRequest(
    @NotNull @Min(0) @Max(3) int playerIndex,
    @NotNull @ValidEnum(enumClass = PlayerCardEventName.class) String cardName) {}
