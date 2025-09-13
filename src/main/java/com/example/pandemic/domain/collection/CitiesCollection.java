package com.example.pandemic.domain.collection;

import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;

import java.util.Collection;

public interface CitiesCollection {

    City get(City.Name cityName);

    boolean isDiseaseEradicated(Disease.Color disease);

    Collection<City> asList();
}
