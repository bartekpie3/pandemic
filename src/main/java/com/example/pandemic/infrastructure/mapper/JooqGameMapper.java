package com.example.pandemic.infrastructure.mapper;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class JooqGameMapper {

  //  public City mapCityFromRecord(GameCitiesRecord record) {
  //    return City.builder()
  //        .name(City.Name.valueOf(record.getName()))
  //        .hasResearchStation(record.getHasResearchStation())
  //        .diseases(
  //            new HashMap<>(4) {
  //              {
  //                put(Disease.Color.RED, record.getRedDisease());
  //                put(Disease.Color.BLACK, record.getBlackDisease());
  //                put(Disease.Color.BLUE, record.getBlueDisease());
  //                put(Disease.Color.YELLOW, record.getYellowDisease());
  //              }
  //            })
  //        .build();
  //  }
}
