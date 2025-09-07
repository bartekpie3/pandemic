package com.example.pandemic.application;

import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.card.*;
import com.example.pandemic.domain.collection.Cities;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class CreateGameHelper {

  private static final int INFECTION_CITIES_PER_TIER = 3;
  private static final int[] INFECTION_TIERS = {3, 2, 1};

  public Game create(@NonNull GameMode gameMode, @NonNull Set<Player.Role> playersRole) {
    var cities = Cities.init();
    var players = createPlayers(playersRole);
    var playerDeck = MemoryCardDeck.initPlayerDeck();
    var infectionDeck = MemoryCardDeck.initInfectionDeck();
    var infectionDiscardedPile = infectCities(cities, infectionDeck);

    playersGetInitialHand(players, playerDeck);
    shuffleEpidemicCards(playerDeck, gameMode);

    return Game.create(players, cities, playerDeck, infectionDeck, infectionDiscardedPile);
  }

  private List<Player> createPlayers(Set<Player.Role> playersRole) {
    return playersRole.stream().map(r -> new Player(r, City.STARTING_CITY_NAME)).toList();
  }

  private void playersGetInitialHand(List<Player> players, CardDeck<PlayerCard> playerDeck) {
    var initialHandSize =
        switch (players.size()) {
          case 2 -> 4;
          case 3 -> 3;
          case 4 -> 2;
          default ->
              throw new IllegalArgumentException(
                  "Unsupported players count: " + players.size() + " (supported: 2, 3, 4)");
        };

    for (var player : players) {
      for (int i = 0; i < initialHandSize; i++) {
        var card = playerDeck.drawCard();
        player.addCard(card);
      }
    }
  }

  private MemoryCardDeck<InfectionCard> infectCities(
      Cities cities, CardDeck<InfectionCard> infectionDeck) {
    MemoryCardDeck<InfectionCard> infectionDiscardedPile = MemoryCardDeck.init();

    for (var tier : INFECTION_TIERS) {
      for (int i = 0; i < INFECTION_CITIES_PER_TIER; i++) {
        var infectionCard = infectionDeck.drawCard();
        cities.get(infectionCard.getCityName()).addDisease(tier);
        infectionDiscardedPile.addCard(infectionCard);
      }
    }

    return infectionDiscardedPile;
  }

  private void shuffleEpidemicCards(MemoryCardDeck<PlayerCard> playerDeck, GameMode gameMode) {
    var cardsNumber =
        switch (gameMode) {
          case EASY -> 4;
          case NORMAL -> 5;
          case HARD -> 6;
        };

    playerDeck.shuffleEvenly(
        IntStream.rangeClosed(1, cardsNumber)
            .mapToObj(i -> PlayerCard.createEpidemicCard())
            .collect(Collectors.toList()));
  }
}
