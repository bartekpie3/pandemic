package com.example.pandemic.domain.dto;

import java.util.Map;

public record CityDto(
    String name, String color, boolean hasResearchStation, Map<String, Integer> diseases) {}
