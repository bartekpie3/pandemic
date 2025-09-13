package com.example.pandemic.infrastructure.repository;

import static com.example.pandemic.domain.model.Disease.Color.*;

import com.example.pandemic.application.query.CityDto;
import com.example.pandemic.application.query.GameDto;
import com.example.pandemic.application.query.PlayerDto;
import com.example.pandemic.application.query.QueryGameRepository;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.GameRepository;
import com.example.pandemic.domain.card.Card;
import com.example.pandemic.infrastructure.mapper.InMemoryQueryGameMapper;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("dev")
@Repository
@RequiredArgsConstructor
public class InMemoryQueryGameRepository implements QueryGameRepository {

  private final GameRepository gameRepository;

  private final InMemoryQueryGameMapper mapper;

  @Override
  public GameDto get(Game.@NonNull Id gameId) {
    return mapper.toDto(gameRepository.get(gameId));
  }

  @Override
  public List<PlayerDto> getPlayers(Game.@NonNull Id gameId) {
    var game = gameRepository.get(gameId);

    return game.getPlayers().stream()
        .map(
            player ->
                new PlayerDto(
                    player.getRole().name(),
                    player.getCurrentLocation().name(),
                    player.getNumberOfAvailableActions(),
                    player.getCardsInHand().stream()
                        .map(Card::getName)
                        .collect(Collectors.toSet())))
        .toList();
  }

  @Override
  public List<CityDto> getCities(Game.@NonNull Id gameId) {
    var game = gameRepository.get(gameId);

    return game.cities().stream()
        .map(
            c -> {
              var cityDiseases = c.getDiseases();
              var diseases = new HashMap<String, Integer>(4);

              diseases.put(BLACK.name(), cityDiseases.get(BLACK));
              diseases.put(BLUE.name(), cityDiseases.get(BLUE));
              diseases.put(YELLOW.name(), cityDiseases.get(YELLOW));
              diseases.put(RED.name(), cityDiseases.get(RED));

              return new CityDto(
                  c.getName().name(), c.getColor().name(), c.hasResearchStation(), diseases);
            })
        .toList();
  }
}
