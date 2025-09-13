package com.example.pandemic.domain.card;

import com.example.common.BaseId;
import com.example.pandemic.domain.model.City;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;

public abstract class Card {

  @Getter @NonNull protected final Id id;

  @Getter @NonNull protected final String name;

  protected Card(@NonNull String name) {
    this.id = Id.generate();
    this.name = name;
  }

  protected Card(@NonNull Card.Id id, @NonNull String name) {
    this.id = id;
    this.name = name;
  }

  public boolean isCityCard(City.@NonNull Name cityName) {
    return cityName.name().equals(name);
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Card card = (Card) o;
    return Objects.equals(name, card.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  public record Id(@NonNull UUID value) implements BaseId {
    public static Id generate() {
      return new Id(UUID.randomUUID());
    }

    public static Id from(UUID id) {
      return new Id(id);
    }
  }
}
