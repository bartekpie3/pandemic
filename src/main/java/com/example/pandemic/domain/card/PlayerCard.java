package com.example.pandemic.domain.card;

import com.example.pandemic.domain.model.City;
import java.util.Optional;

public class PlayerCard extends Card {

  private static final String EPIDEMIC_CARD_NAME = "EPIDEMIC";

  public PlayerCard(String cardName) {
    super(cardName);
  }

  public static PlayerCard createEpidemicCard() {
    return new PlayerCard(EPIDEMIC_CARD_NAME);
  }

  public static PlayerCard createCityCard(City.Name cityName) {
    return new PlayerCard(cityName.name());
  }

  public static PlayerCard createEventCard(PlayerCardEventName event) {
    return new PlayerCard(event.name());
  }

  public static PlayerCard tryFromName(String cardName) {
    return tryCreateCityCard(cardName)
        .orElseGet(
            () ->
                tryCreateEventCard(cardName)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid card name")));
  }

  private static Optional<PlayerCard> tryCreateCityCard(String cardName) {
    try {
      return Optional.of(PlayerCard.createCityCard(City.Name.valueOf(cardName)));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  private static Optional<PlayerCard> tryCreateEventCard(String cardName) {
    try {
      return Optional.of(PlayerCard.createEventCard(PlayerCardEventName.valueOf(cardName)));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  public boolean isInfectionCard() {
    return name.equals(EPIDEMIC_CARD_NAME);
  }
}
