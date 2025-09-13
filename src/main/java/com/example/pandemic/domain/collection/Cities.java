package com.example.pandemic.domain.collection;

import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import lombok.NonNull;

public class Cities implements CitiesCollection {

  private final Map<City.Name, City> cities;

  public Cities(Map<City.Name, City> cities) {
    this.cities = cities;
  }

  public static Cities init() {
    var cities = new EnumMap<City.Name, City>(City.Name.class);

    for (var city : City.Name.values()) {
      cities.put(city, City.of(city));
    }

    cities.put(City.STARTING_CITY_NAME, City.startingCity());

    return new Cities(cities);
  }

  public static boolean areCitiesInSameColor(@NonNull Set<City.Name> cities) {
    if (cities.isEmpty()) {
      return true;
    }

    var color = cities.iterator().next().getColor();

    return cities.stream().allMatch(city -> city.getColor().equals(color));
  }

  public boolean isDiseaseEradicated(Disease.Color disease) {
    return cities.values().stream().noneMatch(city -> city.hasDisease(disease));
  }

  public City get(City.Name cityName) {
    return cities.get(cityName);
  }

  public Collection<City> asList() {
    return cities.values();
  }
}
