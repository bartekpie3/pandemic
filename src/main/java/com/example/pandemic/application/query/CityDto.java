package com.example.pandemic.application.query;

import java.util.Map;

public record CityDto(
    String name, String color, boolean hasResearchStation, Map<String, Integer> diseases) {}
