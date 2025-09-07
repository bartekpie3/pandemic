package com.example.pandemic.userinterface.http.card;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DiscardCardRequest(
    @NotNull @Min(0) @Max(3) int playerIndex, @NotNull String cardName) {}
