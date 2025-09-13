package com.example.pandemic.domain.dto;

import java.util.Set;

// todo add turn order ??

public record PlayerDto(
    String role, String currentLocation, int numberOfAvailableActions, Set<String> cardsInHand) {}
