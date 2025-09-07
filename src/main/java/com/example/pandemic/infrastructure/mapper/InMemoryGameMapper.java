package com.example.pandemic.infrastructure.mapper;

import com.example.pandemic.application.query.DiseaseDto;
import com.example.pandemic.application.query.GameDto;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.model.Disease;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class InMemoryGameMapper {

  public GameDto toDto(Game game) {
    return new GameDto(
        game.getId().value().toString(),
        game.getState(),
        game.getCurrentPlayerTurnIndex(),
        mapToDtoDiseases(game.diseases()),
        game.getOutbreakTrack().getMarkerPosition(),
        game.getInfectionRate());
  }

  private Map<Disease.Color, DiseaseDto> mapToDtoDiseases(Map<Disease.Color, Disease> diseases) {
    return diseases.entrySet().stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                d -> {
                  var disease = d.getValue();

                  return new DiseaseDto(disease.isCured(), disease.isEradicated());
                }));
  }
}
