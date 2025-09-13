package com.example.pandemic.infrastructure.repository.Jooq;

import static com.example.jooq.generated.tables.Cities.CITIES;
import static com.example.jooq.generated.tables.Game.GAME;
import static com.example.jooq.generated.tables.InfectionDeckCards.INFECTION_DECK_CARDS;
import static com.example.jooq.generated.tables.InfectionDiscardPileCards.INFECTION_DISCARD_PILE_CARDS;
import static com.example.jooq.generated.tables.PlayerDeckCards.PLAYER_DECK_CARDS;
import static com.example.jooq.generated.tables.Players.PLAYERS;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

import com.example.jooq.generated.Tables;
import com.example.jooq.generated.tables.records.GameRecord;
import com.example.jooq.generated.tables.records.PlayerCardsRecord;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.GameRepository;
import com.example.pandemic.domain.exception.GameNotFound;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Result;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("prod")
@RequiredArgsConstructor
class JooqGameRepository implements GameRepository {

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

    var createGameHelper = new JooqCreateGameHelper(dsl);
    var gameId = game.getId().value();

    createGameHelper.saveCities(game.cities(), gameId);
    createGameHelper.savePlayers(game.getPlayers(), gameId);
    createGameHelper.savePlayerDeck(game.getPlayerDeck(), gameId);
    createGameHelper.saveInfectionDeck(game.getInfectionDeck(), gameId);
    createGameHelper.saveInfectionDiscardPile(game.getInfectionDiscardPile(), gameId);
  }

  @Override
  public void save(Game game) {
    var gameRecord = prepareGameRecordToSave(game);

    var saved = dsl.update(GAME).set(gameRecord).where(GAME.ID.eq(game.getId().value())).execute();

    if (saved != 1) {
      throw new RuntimeException("Game saving failed");
    }

    var saveGameHelper = new JooqSaveGameHelper(dsl);
    var gameId = game.getId().value();

    saveGameHelper.saveCities(game.cities(), gameId);
    saveGameHelper.savePlayers(game.getPlayers());
    saveGameHelper.savePlayerDeck(game.getPlayerDeck(), gameId);
    saveGameHelper.saveInfectionDeck(game.getInfectionDeck(), gameId);
    saveGameHelper.saveInfectionDiscardPike(game.getInfectionDiscardPile(), gameId);
  }

  @Override
  public void savePlayer(Player player) {
    var saveGameHelper = new JooqSaveGameHelper(dsl);

    saveGameHelper.savePlayers(List.of(player));
  }

  @Override
  public Game get(Game.Id gameId) {
    var gameRecord =
        dsl.selectFrom(GAME)
            .where(GAME.ID.eq(gameId.value()))
            .fetchOptional()
            .orElseThrow(() -> new GameNotFound("Game not found: " + gameId));

    var cities = dsl.selectFrom(CITIES).where(CITIES.GAME_ID.eq(gameId.value())).fetch();
    var players =
        dsl.select(PLAYERS, playerCards())
            .from(PLAYERS)
            .where(PLAYERS.GAME_ID.eq(gameId.value()))
            .orderBy(PLAYERS.TURN_ORDER.asc())
            .fetch();
    var playerDeck =
        dsl.selectFrom(PLAYER_DECK_CARDS)
            .where(PLAYER_DECK_CARDS.GAME_ID.eq(gameId.value()))
            .orderBy(PLAYER_DECK_CARDS.DECK_ORDER.asc())
            .fetch();
    var infectionDeck =
        dsl.selectFrom(INFECTION_DECK_CARDS)
            .where(INFECTION_DECK_CARDS.GAME_ID.eq(gameId.value()))
            .orderBy(INFECTION_DECK_CARDS.DECK_ORDER.asc())
            .fetch();
    var infectionDiscardPile =
        dsl.selectFrom(INFECTION_DISCARD_PILE_CARDS)
            .where(INFECTION_DISCARD_PILE_CARDS.GAME_ID.eq(gameId.value()))
            .orderBy(INFECTION_DISCARD_PILE_CARDS.DECK_ORDER.asc())
            .fetch();

    return mapper.map(gameRecord, cities, players, playerDeck, infectionDeck, infectionDiscardPile);
  }

  private Field<Result<Record1<PlayerCardsRecord>>> playerCards() {
    return multiset(
        select(Tables.PLAYER_CARDS)
            .from(Tables.PLAYER_CARDS)
            .where(Tables.PLAYER_CARDS.PLAYER_ID.eq(Tables.PLAYERS.ID)));
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
}
