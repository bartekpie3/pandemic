package com.example.pandemic.infrastructure.mapper;

import com.example.jooq.generated.tables.records.CitiesRecord;
import com.example.jooq.generated.tables.records.GameRecord;
import com.example.jooq.generated.tables.records.InfectionDeckCardsRecord;
import com.example.jooq.generated.tables.records.InfectionDiscardPileCardsRecord;
import com.example.jooq.generated.tables.records.PlayerCardsRecord;
import com.example.jooq.generated.tables.records.PlayerDeckCardsRecord;
import com.example.jooq.generated.tables.records.PlayersRecord;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.card.Card;
import com.example.pandemic.domain.card.CardDeck;
import com.example.pandemic.domain.card.InfectionCard;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.collection.Cities;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import com.example.pandemic.domain.track.DiseaseTrack;
import com.example.pandemic.domain.track.InfectionRateTrack;
import com.example.pandemic.domain.track.OutbreakTrack;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class JooqGameMapper {
  public Game map(
      GameRecord gameRecord,
      Result<CitiesRecord> cities,
      Result<Record2<PlayersRecord, Result<Record1<PlayerCardsRecord>>>> players,
      Result<PlayerDeckCardsRecord> playerDeck,
      Result<InfectionDeckCardsRecord> infectionDeck,
      Result<InfectionDiscardPileCardsRecord> infectionDiscardPile) {
    return new Game(
        Game.Id.from(gameRecord.getId()),
        new InfectionRateTrack(gameRecord.getInfectionRateMarkerPosition()),
        new OutbreakTrack(gameRecord.getOutbreakMarkerPosition()),
        mapPlayers(players),
        prepareDiseaseTrack(gameRecord),
        mapCities(cities),
        mapPlayerDeck(playerDeck),
        mapPlayerDiscardPile(),
        mapInfectionDeck(infectionDeck),
        mapInfectionDiscardPile(infectionDiscardPile),
        Game.State.valueOf(gameRecord.getState()),
        gameRecord.getCurrentPlayerTurn());
  }

  private CardDeck<InfectionCard> mapInfectionDiscardPile(
      Result<InfectionDiscardPileCardsRecord> infectionDiscardPile) {
    return new JooqCardDeck<>(
        infectionDiscardPile.stream()
            .map(r -> new InfectionCard(Card.Id.from(r.getId()), r.getName()))
            .toList());
  }

  private CardDeck<InfectionCard> mapInfectionDeck(Result<InfectionDeckCardsRecord> infectionDeck) {
    return new JooqCardDeck<>(
        infectionDeck.stream()
            .map(r -> new InfectionCard(Card.Id.from(r.getId()), r.getName()))
            .toList());
  }

  private CardDeck<PlayerCard> mapPlayerDiscardPile() {
    return new JooqCardDeck<>(new ArrayList<>());
  }

  private CardDeck<PlayerCard> mapPlayerDeck(Result<PlayerDeckCardsRecord> playerDeck) {
    return new JooqCardDeck<>(
        playerDeck.stream()
            .map(r -> new PlayerCard(Card.Id.from(r.getId()), r.getName()))
            .toList());
  }

  private Cities mapCities(Result<CitiesRecord> cities) {
    var citiesMap = new EnumMap<City.Name, City>(City.Name.class);

    for (CitiesRecord cityRecord : cities) {
      var diseases = new EnumMap<Disease.Color, Integer>(Disease.Color.class);
      diseases.put(Disease.Color.YELLOW, cityRecord.getYellowDisease());
      diseases.put(Disease.Color.BLACK, cityRecord.getBlackDisease());
      diseases.put(Disease.Color.BLUE, cityRecord.getBlueDisease());
      diseases.put(Disease.Color.RED, cityRecord.getRedDisease());

      citiesMap.put(
          City.Name.valueOf(cityRecord.getName()),
          new City(
              City.Id.from(cityRecord.getId()),
              City.Name.valueOf(cityRecord.getName()),
              diseases,
              cityRecord.getHasResearchStation()));
    }

    return new Cities(citiesMap);
  }

  private DiseaseTrack prepareDiseaseTrack(GameRecord gameRecord) {
    var diseases = new EnumMap<Disease.Color, Disease>(Disease.Color.class);
    diseases.put(
        Disease.Color.YELLOW,
        new Disease(
            Disease.Color.YELLOW,
            gameRecord.getYellowDiseaseIsCured(),
            gameRecord.getYellowDiseaseIsEradicated()));
    diseases.put(
        Disease.Color.BLUE,
        new Disease(
            Disease.Color.BLUE,
            gameRecord.getBlueDiseaseIsCured(),
            gameRecord.getBlueDiseaseIsEradicated()));
    diseases.put(
        Disease.Color.BLACK,
        new Disease(
            Disease.Color.BLACK,
            gameRecord.getBlackDiseaseIsCured(),
            gameRecord.getBlackDiseaseIsEradicated()));
    diseases.put(
        Disease.Color.RED,
        new Disease(
            Disease.Color.RED,
            gameRecord.getRedDiseaseIsCured(),
            gameRecord.getRedDiseaseIsEradicated()));

    return new DiseaseTrack(diseases);
  }

  private List<Player> mapPlayers(
      Result<Record2<PlayersRecord, Result<Record1<PlayerCardsRecord>>>> records) {
    var players = new ArrayList<Player>();

    for (Record2<PlayersRecord, Result<Record1<PlayerCardsRecord>>> r : records) {
      PlayersRecord playerRecord = r.value1();
      Result<Record1<PlayerCardsRecord>> cardsResult = r.value2();

      var player =
          new Player(
              Player.Id.from(playerRecord.getId()),
              Player.Role.valueOf(playerRecord.getRole()),
              mapPlayerCard(cardsResult),
              City.Name.valueOf(playerRecord.getCurrentLocation()),
              playerRecord.getNumberOfAvailableActions(),
              playerRecord.getHasSpecialActionUsed());

      players.add(player);
    }

    return players;
  }

  private List<PlayerCard> mapPlayerCard(Result<Record1<PlayerCardsRecord>> cardsResult) {
    return cardsResult.stream()
        .map(
            cardRecord -> {
              PlayerCardsRecord c = cardRecord.value1();
              return new PlayerCard(Card.Id.from(c.getId()), c.getName());
            })
        .toList();
  }
}
