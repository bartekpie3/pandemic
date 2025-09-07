package com.example.pandemic.support;

import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public final class PlayerBuilder {

  private Player.Role role = Player.Role.MEDIC;

  private City.Name city = City.STARTING_CITY_NAME;

  private List<PlayerCard> cardsInHand = new ArrayList<>();

  private boolean hasSpecialActionUsed = false;

  public static PlayerBuilder aPlayer() {
    return new PlayerBuilder();
  }

  public PlayerBuilder withRole(Player.Role role) {
    this.role = role;
    return this;
  }

  public PlayerBuilder asResearcher() {
    this.role = Player.Role.RESEARCHER;
    return this;
  }

  public PlayerBuilder asOperationsExpert() {
    this.role = Player.Role.OPERATIONS_EXPERT;
    return this;
  }

  public PlayerBuilder asMedic() {
    this.role = Player.Role.MEDIC;
    return this;
  }

  public PlayerBuilder asScientist() {
    this.role = Player.Role.SCIENTIST;
    return this;
  }

  public PlayerBuilder asDispatcher() {
    this.role = Player.Role.DISPATCHER;
    return this;
  }

  public PlayerBuilder inCity(City.Name cityName) {
    this.city = cityName;
    return this;
  }

  public PlayerBuilder withCardsInHand(List<PlayerCard> cardsInHand) {
    this.cardsInHand = cardsInHand;
    return this;
  }

  public PlayerBuilder withUsedSpecialAction() {
    hasSpecialActionUsed = true;
    return this;
  }

  public Player build() {
    var player = new Player(role, city);

    cardsInHand.forEach(player::addCard);

    if (hasSpecialActionUsed) {
      player.useSpecialAction();
    }

    return player;
  }
}
