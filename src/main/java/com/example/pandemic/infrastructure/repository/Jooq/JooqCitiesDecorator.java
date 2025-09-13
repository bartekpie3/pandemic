package com.example.pandemic.infrastructure.repository.Jooq;

import com.example.pandemic.domain.collection.Cities;
import com.example.pandemic.domain.collection.CitiesCollection;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

/**
 * Decorate cities collection to keep track of used cities. To make less queries to database,
 * which cities was used / updated.
 */
class JooqCitiesDecorator implements CitiesCollection {

  private final Cities cities;
  @Getter private final Set<City.Name> usedCities;

  public JooqCitiesDecorator(Cities cities) {
    this.cities = cities;
    this.usedCities = new HashSet<>();
  }

  @Override
  public City get(City.Name cityName) {
    usedCities.add(cityName);

    return cities.get(cityName);
  }

  @Override
  public boolean isDiseaseEradicated(Disease.Color disease) {
    return cities.isDiseaseEradicated(disease);
  }

  @Override
  public Collection<City> asList() {
    return cities.asList();
  }
}
