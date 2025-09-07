package com.example.pandemic.domain.model;

import com.example.common.BaseId;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.exception.PlayerHasNoMoreAvailableActions;
import com.example.pandemic.domain.exception.PlayerHasNoRequiredCard;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
public final class Player {

  private static final int MAX_AVAILABLE_ACTIONS = 4;

  @Getter private final Id id;
  @NonNull @Getter private final Role role;
  @NonNull @Getter private final List<PlayerCard> cardsInHand;
  @NonNull @Getter @Setter private City.Name currentLocation;

  @Getter private int numberOfAvailableActions;

  @Getter private boolean hasSpecialActionUsed;

  public Player(@NonNull Role role, City.@NonNull Name currentLocation) {
    this.id = Id.generate();
    this.role = role;
    this.currentLocation = currentLocation;
    this.cardsInHand = new ArrayList<>();
    this.numberOfAvailableActions = 0;
    this.hasSpecialActionUsed = false;
  }

  public boolean roleIs(Role role) {
    return this.role.equals(role);
  }

  public boolean hasAvailableActions() {
    return numberOfAvailableActions > 0;
  }

  public void resetAvailableActions() {
    numberOfAvailableActions = MAX_AVAILABLE_ACTIONS;
    hasSpecialActionUsed = false;
  }

  public void useSpecialAction() {
    hasSpecialActionUsed = true;
  }

  public PlayerCard getCityCard(City.Name cityCardName) {
    return cardsInHand.stream()
        .filter(c -> c.isCityCard(cityCardName))
        .findFirst()
        .orElseThrow(() -> new PlayerHasNoRequiredCard("Player has no required card"));
  }

  public void pass() {
    numberOfAvailableActions = 0;
  }

  public void takeAction() {
    if (numberOfAvailableActions <= 0) {
      throw new PlayerHasNoMoreAvailableActions("Player has no more available actions");
    }

    numberOfAvailableActions--;
  }

  public boolean hasCityCard(City.Name cityName) {
    return cardsInHand.stream().anyMatch(c -> c.isCityCard(cityName));
  }

  public boolean hasCard(PlayerCard playerCard) {
    return cardsInHand.contains(playerCard);
  }

  public void discardCard(PlayerCard playerCard) {
    cardsInHand.remove(playerCard);
  }

  public void addCard(PlayerCard card) {
    cardsInHand.add(card);
  }

  public int getNumberOfCardsInHand() {
    return cardsInHand.size();
  }

  @Override
  public String toString() {
    return role.name();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    return role == player.role;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(role);
  }

  public enum Role {
    MEDIC,
    OPERATIONS_EXPERT,
    SCIENTIST,
    RESEARCHER,
    DISPATCHER,
    CONTINGENCY_PLANNER,
    QUARANTINE_SPECIALIST
  }

  public record Id(UUID value) implements BaseId {
    public static Id generate() {
      return new Id(UUID.randomUUID());
    }
  }
}
