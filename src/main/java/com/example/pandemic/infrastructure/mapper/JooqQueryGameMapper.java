package com.example.pandemic.infrastructure.mapper;

import com.example.jooq.generated.tables.records.CitiesRecord;
import com.example.jooq.generated.tables.records.GameRecord;
import com.example.jooq.generated.tables.records.PlayersRecord;
import com.example.pandemic.application.query.CityDto;
import com.example.pandemic.application.query.DiseaseDto;
import com.example.pandemic.application.query.GameDto;
import com.example.pandemic.application.query.PlayerDto;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.springframework.stereotype.Component;

@Component
public class JooqQueryGameMapper {
  public GameDto mapGame(GameRecord gameRecord) {
    return new GameDto(
        gameRecord.getId().toString(),
        gameRecord.getState(),
        gameRecord.getCurrentPlayerTurn(),
        prepareDiseases(gameRecord),
        gameRecord.getOutbreakMarkerPosition(),
        gameRecord.getInfectionRateMarkerPosition());
  }

  private Map<Disease.Color, DiseaseDto> prepareDiseases(GameRecord gameRecord) {
    var diseases = new EnumMap<Disease.Color, DiseaseDto>(Disease.Color.class);
    diseases.put(
        Disease.Color.YELLOW,
        new DiseaseDto(
            gameRecord.getYellowDiseaseIsCured(), gameRecord.getYellowDiseaseIsEradicated()));
    diseases.put(
        Disease.Color.BLACK,
        new DiseaseDto(
            gameRecord.getBlackDiseaseIsCured(), gameRecord.getBlackDiseaseIsEradicated()));
    diseases.put(
        Disease.Color.BLUE,
        new DiseaseDto(
            gameRecord.getBlueDiseaseIsCured(), gameRecord.getBlueDiseaseIsEradicated()));
    diseases.put(
        Disease.Color.RED,
        new DiseaseDto(gameRecord.getRedDiseaseIsCured(), gameRecord.getRedDiseaseIsEradicated()));

    return diseases;
  }

  public PlayerDto mapPlayer(Record2<PlayersRecord, Result<Record1<String>>> playersRecordResult) {
    var playersRecord = playersRecordResult.component1();

    return new PlayerDto(
        playersRecord.getRole(),
        playersRecord.getCurrentLocation(),
        playersRecord.getNumberOfAvailableActions(),
        mapPlayerCards(playersRecordResult.component2()));
  }

  private Set<String> mapPlayerCards(Result<Record1<String>> cards) {
    return cards.stream().map(Record1::value1).collect(Collectors.toSet());
  }

  public CityDto mapCity(CitiesRecord citiesRecord) {
    var diseases = new HashMap<String, Integer>(4);
    diseases.put(Disease.Color.RED.name(), citiesRecord.getRedDisease());
    diseases.put(Disease.Color.BLUE.name(), citiesRecord.getBlueDisease());
    diseases.put(Disease.Color.BLACK.name(), citiesRecord.getBlackDisease());
    diseases.put(Disease.Color.YELLOW.name(), citiesRecord.getYellowDisease());

    return new CityDto(
        citiesRecord.getName(),
        City.Name.valueOf(citiesRecord.getName()).getColor().name(),
        citiesRecord.getHasResearchStation(),
        diseases);
  }
}
