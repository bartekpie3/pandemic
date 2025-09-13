package com.example.pandemic.infrastructure.repository.Jooq;

import static com.example.jooq.generated.tables.Cities.CITIES;
import static com.example.jooq.generated.tables.InfectionDeckCards.INFECTION_DECK_CARDS;
import static com.example.jooq.generated.tables.InfectionDiscardPileCards.INFECTION_DISCARD_PILE_CARDS;
import static com.example.jooq.generated.tables.PlayerCards.PLAYER_CARDS;
import static com.example.jooq.generated.tables.PlayerDeckCards.PLAYER_DECK_CARDS;
import static com.example.jooq.generated.tables.Players.PLAYERS;

import com.example.jooq.generated.tables.records.PlayerCardsRecord;
import com.example.pandemic.domain.card.Card;
import com.example.pandemic.domain.card.CardDeck;
import com.example.pandemic.domain.card.InfectionCard;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.collection.CitiesCollection;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;

record JooqSaveGameHelper(DSLContext dsl) {

  public void savePlayers(List<Player> players) {
    for (var player : players) {
      var saved =
          dsl.update(PLAYERS)
              .set(PLAYERS.CURRENT_LOCATION, player.getCurrentLocation().name())
              .set(PLAYERS.HAS_SPECIAL_ACTION_USED, player.hasSpecialActionUsed())
              .set(PLAYERS.NUMBER_OF_AVAILABLE_ACTIONS, player.getNumberOfAvailableActions())
              .where(PLAYERS.ID.eq(player.getId().value()))
              .execute();

      if (saved != 1) {
        throw new RuntimeException("Player saving failed");
      }

      savePlayerCards(player);
    }
  }

  private void savePlayerCards(Player player) {
    dsl.deleteFrom(PLAYER_CARDS).where(PLAYER_CARDS.PLAYER_ID.eq(player.getId().value())).execute();

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

  private PlayerCardsRecord preparePlayerCardRecord(PlayerCard card, Player player) {
    return new PlayerCardsRecord(card.getId().value(), player.getId().value(), card.getName());
  }

  public void saveCities(@NonNull CitiesCollection cities, UUID gameId) {
    if (cities instanceof JooqCitiesDecorator jooqCities) {
      var usedCities = jooqCities.getUsedCities();

      for (var cityName : usedCities) {
        var city = jooqCities.get(cityName);

        var saved =
            dsl.update(CITIES)
                .set(CITIES.HAS_RESEARCH_STATION, city.hasResearchStation())
                .set(CITIES.BLACK_DISEASE, city.getDiseases().get(Disease.Color.BLACK))
                .set(CITIES.BLUE_DISEASE, city.getDiseases().get(Disease.Color.BLUE))
                .set(CITIES.YELLOW_DISEASE, city.getDiseases().get(Disease.Color.YELLOW))
                .set(CITIES.RED_DISEASE, city.getDiseases().get(Disease.Color.RED))
                .where(CITIES.ID.eq(city.getId().value()))
                .execute();

        if (saved != 1) {
          throw new RuntimeException("City saving failed");
        }
      }
    } else {
      throw new RuntimeException("Invalid cities - required JooqCitiesDecorator");
    }
  }

  public void saveInfectionDeck(CardDeck<InfectionCard> infectionDeck, UUID gameId) {
    saveDeck(
        infectionDeck,
        INFECTION_DECK_CARDS,
        INFECTION_DECK_CARDS.ID,
        INFECTION_DECK_CARDS.NAME,
        INFECTION_DECK_CARDS.GAME_ID,
        INFECTION_DECK_CARDS.DECK_ORDER,
        gameId);
  }

  public void saveInfectionDiscardPike(CardDeck<InfectionCard> infectionDiscardPile, UUID gameId) {
    saveDeck(
        infectionDiscardPile,
        INFECTION_DISCARD_PILE_CARDS,
        INFECTION_DISCARD_PILE_CARDS.ID,
        INFECTION_DISCARD_PILE_CARDS.NAME,
        INFECTION_DISCARD_PILE_CARDS.GAME_ID,
        INFECTION_DISCARD_PILE_CARDS.DECK_ORDER,
        gameId);
  }

  public void savePlayerDeck(CardDeck<PlayerCard> playerDeck, UUID gameId) {
    saveDeck(
        playerDeck,
        PLAYER_DECK_CARDS,
        PLAYER_DECK_CARDS.ID,
        PLAYER_DECK_CARDS.NAME,
        PLAYER_DECK_CARDS.GAME_ID,
        PLAYER_DECK_CARDS.DECK_ORDER,
        gameId);
  }

  private <T extends Card> void saveDeck(
      CardDeck<T> deck,
      Table<?> table,
      TableField<?, UUID> idField,
      TableField<?, String> nameField,
      TableField<?, UUID> gameField,
      TableField<?, Integer> deckOrderField,
      UUID gameId) {
    if (deck instanceof JooqCardDeck<T> jooqDeck) {
      removeFromTableRemovedCards(jooqDeck, table, idField);
      addToTableAddedCards(jooqDeck, table, idField, nameField, gameField, deckOrderField, gameId);
    } else {
      throw new RuntimeException("Invalid deck - required JooqCardDeck");
    }
  }

  private <T extends Card> void removeFromTableRemovedCards(
      JooqCardDeck<T> jooqDeck, Table<?> table, TableField<?, UUID> idField) {
    jooqDeck
        .getRemovedCards()
        .forEach(
            card -> {
              int removed = dsl.deleteFrom(table).where(idField.eq(card.getId().value())).execute();

              if (removed != 1) {
                throw new RuntimeException("Deck card removing failed for " + card);
              }
            });
  }

  private <T extends Card> void addToTableAddedCards(
      JooqCardDeck<T> jooqDeck,
      Table<?> table,
      TableField<?, UUID> idField,
      TableField<?, String> nameField,
      TableField<?, UUID> gameField,
      TableField<?, Integer> deckOrderField,
      UUID gameId) {
    if (!jooqDeck.getAddedCards().isEmpty()) {
      var lastDeckOrder = findLastOrderInDeck(table, gameField, deckOrderField, gameId);
      var deckOrder = lastDeckOrder.orElse(0) + 1;

      for (var card : jooqDeck.getAddedCards()) {
        var saved =
            dsl.insertInto(table)
                .set(idField, card.getId().value())
                .set(nameField, card.getName())
                .set(gameField, gameId)
                .set(deckOrderField, deckOrder)
                .execute();

        if (saved != 1) {
          throw new RuntimeException("Deck card saving failed for " + card);
        }

        deckOrder++;
      }
    }
  }

  private Optional<Integer> findLastOrderInDeck(
      Table<?> table,
      TableField<?, UUID> gameField,
      TableField<?, Integer> deckOrderField,
      UUID gameId) {

    return dsl.select(deckOrderField)
        .from(table)
        .where(gameField.eq(gameId))
        .orderBy(deckOrderField.desc())
        .limit(1)
        .fetchOptional(r -> r.get(deckOrderField, Integer.class));
  }
}
