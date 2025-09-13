package com.example.pandemic.domain.model;

import static com.example.pandemic.domain.model.Disease.Color.*;

import com.example.common.BaseId;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public final class City {

  public static final Name STARTING_CITY_NAME = Name.ATLANTA;

  public static final int MAX_DISEASE_IN_CITY = 3;

  @Getter private final Id id;

  @NonNull @Getter private final Name name;
  @Getter private final Map<Disease.Color, Integer> diseases;
  private boolean hasResearchStation;

  public City(@NonNull Name name) {
    this.id = Id.generate();
    this.name = name;
    this.diseases = initDiseaseMap();
    this.hasResearchStation = false;
  }

  public static City of(Name name) {
    return new City(name);
  }

  public static City startingCity() {
    var city = new City(STARTING_CITY_NAME);
    city.hasResearchStation = true;

    return city;
  }

  private static Map<Disease.Color, Integer> initDiseaseMap() {
    var map = new EnumMap<Disease.Color, Integer>(Disease.Color.class);

    for (var color : Disease.Color.values()) {
      map.put(color, 0);
    }

    return map;
  }

  public Disease.Color getColor() {
    return name.color;
  }

  public void buildResearchStation() {
    this.hasResearchStation = true;
  }

  public void removeResearchStation() {
    this.hasResearchStation = false;
  }

  /**
   * @return boolean did outbreaks happen?
   */
  public boolean addDisease(int infected) {
    return addDisease(getColor(), infected);
  }

  /**
   * @return boolean did outbreaks happen?
   */
  public boolean addDisease(@NonNull Disease.Color disease, int infected) {
    var currentValue = diseases.get(disease);
    var newValue = currentValue + infected;
    var isOutbreak = newValue > MAX_DISEASE_IN_CITY;

    diseases.put(disease, Math.min(newValue, MAX_DISEASE_IN_CITY));

    return isOutbreak;
  }

  /**
   * @return int how many diseases have been cured
   */
  public int treatDisease(@NonNull Disease.Color disease, int toTreat) {
    var currentDiseaseValue = diseases.get(disease);
    var newValue = Math.max(currentDiseaseValue - toTreat, 0);

    diseases.put(disease, newValue);

    return Math.max(currentDiseaseValue - newValue, 0);
  }

  public boolean hasResearchStation() {
    return hasResearchStation;
  }

  public boolean hasDisease(Disease.Color disease) {
    return diseases.get(disease) > 0;
  }

  @Override
  public String toString() {
    return name.name();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    City city = (City) o;
    return name == city.name;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  @Getter
  public enum Name {
    // BLUE
    ATLANTA(BLUE),
    CHICAGO(BLUE),
    WASHINGTON(BLUE),
    NEW_YORK(BLUE),
    MONTREAL(BLUE),
    SAN_FRANCISCO(BLUE),
    MADRID(BLUE),
    PARIS(BLUE),
    LONDON(BLUE),
    ESSEN(BLUE),
    MILAN(BLUE),
    PETERSBURG(BLUE),

    // YEllOW
    MIAMI(YELLOW),
    MEXICO(YELLOW),
    LOS_ANGELES(YELLOW),
    BOGOTA(YELLOW),
    SAO_PAOLO(YELLOW),
    LIMA(YELLOW),
    SANTIAGO(YELLOW),
    BUENOS_AIRES(YELLOW),
    LAGOS(YELLOW),
    KHARTOUM(YELLOW),
    KINSHASA(YELLOW),
    JOHANNESBURG(YELLOW),

    // RED
    SYDNEY(RED),
    MANILA(RED),
    HO_CHI_MINH(RED),
    JAKARTA(RED),
    BANGKOK(RED),
    HONG_KONG(RED),
    TAIPEI(RED),
    OSAKA(RED),
    SHANGHAI(RED),
    BEIJING(RED),
    SEOUL(RED),
    TOKYO(RED),

    // BLACK
    MOSCOW(BLACK),
    DELHI(BLACK),
    TEHRAN(BLACK),
    BAGHDAD(BLACK),
    RIYADH(BLACK),
    KARACHI(BLACK),
    KOLKATA(BLACK),
    CHENNAI(BLACK),
    MUMBAI(BLACK),
    ALGIERS(BLACK),
    CAIRO(BLACK),
    ISTANBUL(BLACK);

    private final Disease.Color color;

    Name(Disease.Color color) {
      this.color = color;
    }
  }

  public record Id(UUID value) implements BaseId {
    public static Id generate() {
      return new Id(UUID.randomUUID());
    }

    public static Id from(UUID id) {
      return new Id(id);
    }
  }
}
