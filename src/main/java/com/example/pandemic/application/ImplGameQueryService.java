package com.example.pandemic.application;

import com.example.pandemic.domain.dto.CityDto;
import com.example.pandemic.domain.dto.GameDto;
import com.example.pandemic.domain.dto.PlayerDto;
import com.example.pandemic.domain.QueryGameRepository;
import com.example.pandemic.domain.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class ImplGameQueryService implements GameQueryService {

  private final QueryGameRepository queryGameRepository;

  @Override
  public GameDto getGame(Game.@NonNull Id gameId) {
    return queryGameRepository.get(gameId);
  }

  @Override
  public List<PlayerDto> getPlayers(Game.@NonNull Id gameId) {
    return queryGameRepository.getPlayers(gameId);
  }

  @Override
  public List<CityDto> getCities(Game.@NonNull Id gameId) {
    return queryGameRepository.getCities(gameId);
  }
}
