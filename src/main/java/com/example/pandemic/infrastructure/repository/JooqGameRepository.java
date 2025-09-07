package com.example.pandemic.infrastructure.repository;

import static com.example.jooq.generated.tables.Cities.CITIES;
import static com.example.jooq.generated.tables.Game.GAME;
import static com.example.jooq.generated.tables.InfectionDeckCards.INFECTION_DECK_CARDS;
import static com.example.jooq.generated.tables.InfectionDiscardPileCards.INFECTION_DISCARD_PILE_CARDS;
import static com.example.jooq.generated.tables.PlayerCards.PLAYER_CARDS;
import static com.example.jooq.generated.tables.PlayerDeckCards.PLAYER_DECK_CARDS;
import static com.example.jooq.generated.tables.Players.PLAYERS;

import com.example.jooq.generated.tables.records.GameRecord;
import com.example.jooq.generated.tables.records.PlayerCardsRecord;
import com.example.jooq.generated.tables.records.PlayersRecord;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.GameRepository;
import com.example.pandemic.domain.card.Card;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import com.example.pandemic.infrastructure.mapper.JooqGameMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("prod")
@RequiredArgsConstructor
public class JooqGameRepository implements GameRepository {

  private final JooqGameMapper mapper;

  private final DSLContext dsl;

  @Override
  public void create(Game game) {
    var gameRecord = prepareGameRecordToSave(game);

    var saved =
        dsl.insertInto(GAME).set(gameRecord).set(GAME.CREATED_AT, OffsetDateTime.now()).execute();

    if (saved != 1) {
      throw new RuntimeException("Game creation failed");
    }

    saveCities(game);
    savePlayers(game);
    savePlayerDeck(game);
    saveInfectionDeck(game);
    saveInfectionDiscardPile(game);
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

  private void saveInfectionDiscardPile(Game game) {
    saveDeck(
        INFECTION_DISCARD_PILE_CARDS,
        INFECTION_DISCARD_PILE_CARDS.ID,
        INFECTION_DISCARD_PILE_CARDS.NAME,
        INFECTION_DISCARD_PILE_CARDS.GAME_ID,
        INFECTION_DISCARD_PILE_CARDS.DECK_ORDER,
        game.getInfectionDiscardPile().getCards(),
        game.getId().value());
  }

  private void saveInfectionDeck(Game game) {
    saveDeck(
        INFECTION_DECK_CARDS,
        INFECTION_DECK_CARDS.ID,
        INFECTION_DECK_CARDS.NAME,
        INFECTION_DECK_CARDS.GAME_ID,
        INFECTION_DECK_CARDS.DECK_ORDER,
        game.getInfectionDeck().getCards(),
        game.getId().value());
  }

  private void savePlayerDeck(Game game) {
    saveDeck(
        PLAYER_DECK_CARDS,
        PLAYER_DECK_CARDS.ID,
        PLAYER_DECK_CARDS.NAME,
        PLAYER_DECK_CARDS.GAME_ID,
        PLAYER_DECK_CARDS.DECK_ORDER,
        game.getPlayerDeck().getCards(),
        game.getId().value());
  }

  private GameRecord prepareGameRecordToSave(Game game) {
    var gameDiseases = game.diseases();
    var gameRecord = new GameRecord();

    gameRecord.setId(game.getId().value());
    gameRecord.set(GAME.CURRENT_PLAYER_TURN, game.getCurrentPlayerTurnIndex());
    gameRecord.set(GAME.OUTBREAK_MARKER_POSITION, game.getOutbreakTrack().getMarkerPosition());
    gameRecord.set(
        GAME.INFECTION_RATE_MARKER_POSITION, game.getInfectionRateTrack().getMarkerPosition());
    gameRecord.set(GAME.STATE, game.getState());
    gameRecord.set(GAME.YELLOW_DISEASE_IS_CURED, gameDiseases.get(Disease.Color.YELLOW).isCured());
    gameRecord.set(
        GAME.YELLOW_DISEASE_IS_ERADICATED, gameDiseases.get(Disease.Color.YELLOW).isEradicated());
    gameRecord.set(GAME.RED_DISEASE_IS_CURED, gameDiseases.get(Disease.Color.RED).isCured());
    gameRecord.set(
        GAME.RED_DISEASE_IS_ERADICATED, gameDiseases.get(Disease.Color.RED).isEradicated());
    gameRecord.set(GAME.BLUE_DISEASE_IS_CURED, gameDiseases.get(Disease.Color.BLUE).isCured());
    gameRecord.set(
        GAME.BLUE_DISEASE_IS_ERADICATED, gameDiseases.get(Disease.Color.BLUE).isEradicated());
    gameRecord.set(GAME.BLACK_DISEASE_IS_CURED, gameDiseases.get(Disease.Color.BLACK).isCured());
    gameRecord.set(
        GAME.BLACK_DISEASE_IS_ERADICATED, gameDiseases.get(Disease.Color.BLACK).isEradicated());

    return gameRecord;
  }

  private void saveCities(Game game) {
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

  private void savePlayers(Game game) {
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

  @Override
  public void save(Game game) {}

  @Override
  public Game get(Game.Id gameId) {
    return null;
  }
}
