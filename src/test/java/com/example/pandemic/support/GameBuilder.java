package com.example.pandemic.support;

import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.card.CardDeck;
import com.example.pandemic.domain.card.InfectionCard;
import com.example.pandemic.domain.card.MemoryCardDeck;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.collection.Cities;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import com.example.pandemic.domain.track.DiseaseTrack;
import com.example.pandemic.domain.track.InfectionRateTrack;
import com.example.pandemic.domain.track.OutbreakTrack;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameBuilder {

  private final DiseaseTrack diseaseTrack = DiseaseTrack.init();
  private List<Player> players;
  private int outbreakMarkerPosition = 0;
  private int infectionRateMarkerPosition = 0;
  private CardDeck<PlayerCard> playerDeck = MemoryCardDeck.initPlayerDeck();
  private CardDeck<InfectionCard> infectionDeck = MemoryCardDeck.initInfectionDeck();
  private CardDeck<InfectionCard> infectionDiscardPile = MemoryCardDeck.init();
  private Game.State state = Game.State.ACTION;

  private GameBuilder() {
    players = new ArrayList<>();

    players.add(PlayerBuilder.aPlayer().asMedic().build());
    players.add(PlayerBuilder.aPlayer().asScientist().build());
  }

  public static GameBuilder aGame() {
    return new GameBuilder();
  }

  public GameBuilder withBlackDiseaseEradicated() {
    diseaseTrack.cureDisease(Disease.Color.BLACK);
    diseaseTrack.eradicateDisease(Disease.Color.BLACK);

    return this;
  }

  public GameBuilder withPlayers(Set<Player.Role> players) {
    this.players = new ArrayList<>(players.size());
    players.forEach(role -> this.players.add(PlayerBuilder.aPlayer().withRole(role).build()));

    return this;
  }

  public GameBuilder withPlayers(List<Player> players) {
    this.players = players;

    return this;
  }

  public GameBuilder withActivePlayer(Player player) {
    this.players.set(0, player);

    return this;
  }

  public GameBuilder withOutbreakMarkerPosition(int outbreakMarkerPosition) {
    this.outbreakMarkerPosition = outbreakMarkerPosition;

    return this;
  }

  public GameBuilder withInfectionRateMarkerPosition(int infectionRateMarkerPosition) {
    this.infectionRateMarkerPosition = infectionRateMarkerPosition;

    return this;
  }

  public GameBuilder withInfectionDeck(MemoryCardDeck<InfectionCard> infectionDeck) {
    this.infectionDeck = infectionDeck;

    return this;
  }

  public GameBuilder withInfectionDiscardPile(MemoryCardDeck<InfectionCard> infectionDiscardPile) {
    this.infectionDiscardPile = infectionDiscardPile;

    return this;
  }

  public GameBuilder withPlayerDeck(MemoryCardDeck<PlayerCard> playerDeck) {
    this.playerDeck = playerDeck;

    return this;
  }

  public GameBuilder onFirstDrawState() {
    this.state = Game.State.FIRST_DRAW;

    return this;
  }

  public GameBuilder onInfectState() {
    this.state = Game.State.INFECT;

    return this;
  }

  public GameBuilder onSecondDrawStep() {
    this.state = Game.State.SECOND_DRAW;

    return this;
  }

  public Game build() {
    players.getFirst().resetAvailableActions();

    return new Game(
        Game.Id.generate(),
        new InfectionRateTrack(infectionRateMarkerPosition),
        new OutbreakTrack(outbreakMarkerPosition),
        players,
        diseaseTrack,
        Cities.init(),
        playerDeck,
        MemoryCardDeck.init(),
        infectionDeck,
        infectionDiscardPile,
        state,
        0);
  }
}
