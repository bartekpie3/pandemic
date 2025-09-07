package com.example.pandemic.application.query;

import java.util.Set;

// todo add turn order ??

public record PlayerDto(
    String role, String currentLocation, int numberOfAvailableActions, Set<String> cardsInHand) {}
