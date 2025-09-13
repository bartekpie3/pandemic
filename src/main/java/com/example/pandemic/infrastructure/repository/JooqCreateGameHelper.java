package com.example.pandemic.infrastructure.repository;

import static com.example.jooq.generated.tables.Cities.CITIES;
import static com.example.jooq.generated.tables.InfectionDeckCards.INFECTION_DECK_CARDS;
import static com.example.jooq.generated.tables.InfectionDiscardPileCards.INFECTION_DISCARD_PILE_CARDS;
import static com.example.jooq.generated.tables.PlayerCards.PLAYER_CARDS;
import static com.example.jooq.generated.tables.PlayerDeckCards.PLAYER_DECK_CARDS;
import static com.example.jooq.generated.tables.Players.PLAYERS;

import com.example.jooq.generated.tables.records.PlayerCardsRecord;
import com.example.jooq.generated.tables.records.PlayersRecord;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.card.Card;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;

@RequiredArgsConstructor
public class JooqCreateGameHelper {

  private final DSLContext dsl;

  public void createInfectionDiscardPile(Game game) {
    saveDeck(
        INFECTION_DISCARD_PILE_CARDS,
        INFECTION_DISCARD_PILE_CARDS.ID,
        INFECTION_DISCARD_PILE_CARDS.NAME,
        INFECTION_DISCARD_PILE_CARDS.GAME_ID,
        INFECTION_DISCARD_PILE_CARDS.DECK_ORDER,
        game.getInfectionDiscardPile().getCards(),
        game.getId().value());
  }

  public void createInfectionDeck(Game game) {
    saveDeck(
        INFECTION_DECK_CARDS,
        INFECTION_DECK_CARDS.ID,
        INFECTION_DECK_CARDS.NAME,
        INFECTION_DECK_CARDS.GAME_ID,
        INFECTION_DECK_CARDS.DECK_ORDER,
        game.getInfectionDeck().getCards(),
        game.getId().value());
  }

  public void createPlayerDeck(Game game) {
    saveDeck(
        PLAYER_DECK_CARDS,
        PLAYER_DECK_CARDS.ID,
        PLAYER_DECK_CARDS.NAME,
        PLAYER_DECK_CARDS.GAME_ID,
        PLAYER_DECK_CARDS.DECK_ORDER,
        game.getPlayerDeck().getCards(),
        game.getId().value());
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

    if (saved == 0) {
      throw new RuntimeException("Deck " + table + " saving failed");
    }
  }

  public void createCities(Game game) {
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

    for (var city : game.cities().asList()) {
      var cityDiseases = city.getDiseases();

      insert =
          insert.values(
              city.getId().value(),
              game.getId().value(),
              city.getName().name(),
              city.hasResearchStation(),
              cityDiseases.get(Disease.Color.BLACK),
              cityDiseases.get(Disease.Color.RED),
              cityDiseases.get(Disease.Color.BLUE),
              cityDiseases.get(Disease.Color.YELLOW));
    }

    var saved = insert.execute();

    if (saved == 0) {
      throw new RuntimeException("Cities saving failed");
    }
  }

  public void createPlayers(Game game) {
    var turnOrder = 0;

    for (var player : game.getPlayers()) {
      var playerRecord = preparePlayerRecord(player, game, turnOrder);
      var saved = dsl.insertInto(PLAYERS).set(playerRecord).execute();

      for (var card : player.getCardsInHand()) {
        dsl.insertInto(PLAYER_CARDS).set(preparePlayerCardRecord(card, player)).execute();
      }

      if (saved != 1) {
        throw new RuntimeException("Player saving failed");
      }

      turnOrder++;
    }
  }

  private PlayerCardsRecord preparePlayerCardRecord(PlayerCard card, Player player) {
    return new PlayerCardsRecord(card.getId().value(), player.getId().value(), card.getName());
  }

  private PlayersRecord preparePlayerRecord(Player player, Game game, int turnOrder) {
    return new PlayersRecord(
        player.getId().value(),
        game.getId().value(),
        player.getRole().name(),
        player.getCurrentLocation().name(),
        player.getNumberOfAvailableActions(),
        player.isHasSpecialActionUsed(),
        turnOrder);
  }
}
