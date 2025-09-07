package com.example.pandemic.application;

import com.example.pandemic.application.query.CityDto;
import com.example.pandemic.application.query.GameDto;
import com.example.pandemic.application.query.PlayerDto;
import com.example.pandemic.domain.Game;
import lombok.NonNull;

import java.util.List;

public interface GameQueryService {

  GameDto getGame(Game.@NonNull Id gameId);

  List<PlayerDto> getPlayers(Game.@NonNull Id gameId);

  List<CityDto> getCities(Game.@NonNull Id gameId);
}
