package com.example.pandemic.infrastructure.repository.Jooq;

import static com.example.jooq.generated.tables.Cities.CITIES;
import static com.example.jooq.generated.tables.InfectionDeckCards.INFECTION_DECK_CARDS;
import static com.example.jooq.generated.tables.InfectionDiscardPileCards.INFECTION_DISCARD_PILE_CARDS;
import static com.example.jooq.generated.tables.PlayerCards.PLAYER_CARDS;
import static com.example.jooq.generated.tables.PlayerDeckCards.PLAYER_DECK_CARDS;
import static com.example.jooq.generated.tables.Players.PLAYERS;

import com.example.jooq.generated.tables.records.PlayersRecord;
import com.example.pandemic.domain.card.Card;
import com.example.pandemic.domain.card.CardDeck;
import com.example.pandemic.domain.card.InfectionCard;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.collection.CitiesCollection;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import java.util.List;
import java.util.UUID;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;

record JooqCreateGameHelper(DSLContext dsl) {

  public void saveInfectionDiscardPile(CardDeck<InfectionCard> infectionDiscardPile, UUID gameId) {
    saveDeck(
        INFECTION_DISCARD_PILE_CARDS,
        INFECTION_DISCARD_PILE_CARDS.ID,
        INFECTION_DISCARD_PILE_CARDS.NAME,
        INFECTION_DISCARD_PILE_CARDS.GAME_ID,
        INFECTION_DISCARD_PILE_CARDS.DECK_ORDER,
        infectionDiscardPile.getCards(),
        gameId);
  }

  public void saveInfectionDeck(CardDeck<InfectionCard> infectionDeck, UUID gameId) {
    saveDeck(
        INFECTION_DECK_CARDS,
        INFECTION_DECK_CARDS.ID,
        INFECTION_DECK_CARDS.NAME,
        INFECTION_DECK_CARDS.GAME_ID,
        INFECTION_DECK_CARDS.DECK_ORDER,
        infectionDeck.getCards(),
        gameId);
  }

  public void savePlayerDeck(CardDeck<PlayerCard> playerDeck, UUID gameId) {
    saveDeck(
        PLAYER_DECK_CARDS,
        PLAYER_DECK_CARDS.ID,
        PLAYER_DECK_CARDS.NAME,
        PLAYER_DECK_CARDS.GAME_ID,
        PLAYER_DECK_CARDS.DECK_ORDER,
        playerDeck.getCards(),
        gameId);
  }

  private <T extends Record> void saveDeck(
      Table<T> table,
      TableField<T, UUID> idField,
      TableField<T, String> nameField,
      TableField<T, UUID> gameIdField,
      TableField<T, Integer> orderField,
      List<Card> cards,
      UUID gameId) {
    var insert = dsl.insertInto(table, idField, nameField, gameIdField, orderField);
    var deckOrder = 0;

    for (var card : cards) {
      insert = insert.values(card.getId().value(), card.getName(), gameId, deckOrder);

      deckOrder++;
    }

    var saved = insert.execute();

    if (saved != cards.size()) {
      throw new RuntimeException("Deck " + table + " creating failed");
    }
  }

  public void saveCities(CitiesCollection cities, UUID gameId) {
    var insert =
        dsl.insertInto(
            CITIES,
            CITIES.ID,
            CITIES.GAME_ID,
            CITIES.NAME,
            CITIES.HAS_RESEARCH_STATION,
            CITIES.BLACK_DISEASE,
            CITIES.RED_DISEASE,
            CITIES.BLUE_DISEASE,
            CITIES.YELLOW_DISEASE);

    for (var city : cities.asList()) {
      var cityDiseases = city.getDiseases();

      insert =
          insert.values(
              city.getId().value(),
              gameId,
              city.getName().name(),
              city.hasResearchStation(),
              cityDiseases.get(Disease.Color.BLACK),
              cityDiseases.get(Disease.Color.RED),
              cityDiseases.get(Disease.Color.BLUE),
              cityDiseases.get(Disease.Color.YELLOW));
    }

    var saved = insert.execute();

    if (saved == 0) {
      throw new RuntimeException("Cities creating failed");
    }
  }

  public void savePlayers(List<Player> players, UUID gameId) {
    var turnOrder = 0;

    for (var player : players) {
      var playerRecord = preparePlayerRecord(player, gameId, turnOrder);
      var saved = dsl.insertInto(PLAYERS).set(playerRecord).execute();

      if (saved != 1) {
        throw new RuntimeException("Player creating failed");
      }

      savePlayerCards(player);

      turnOrder++;
    }
  }

  private void savePlayerCards(Player player) {
    var insert =
        dsl.insertInto(PLAYER_CARDS, PLAYER_CARDS.ID, PLAYER_CARDS.PLAYER_ID, PLAYER_CARDS.NAME);

    for (var card : player.getCardsInHand()) {
      insert = insert.values(card.getId().value(), player.getId().value(), card.getName());
    }

    var savedCards = insert.execute();

    if (savedCards != player.getCardsInHand().size()) {
      throw new RuntimeException("Player card creating failed");
    }
  }

  protected PlayersRecord preparePlayerRecord(Player player, UUID gameId, int turnOrder) {
    return new PlayersRecord(
        player.getId().value(),
        gameId,
        player.getRole().name(),
        player.getCurrentLocation().name(),
        player.getNumberOfAvailableActions(),
        player.hasSpecialActionUsed(),
        turnOrder);
  }
}
