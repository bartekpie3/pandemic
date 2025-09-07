package com.example.pandemic.application.query;

import com.example.pandemic.domain.Game;
import lombok.NonNull;

import java.util.List;

public interface QueryGameRepository {

    GameDto get(Game.@NonNull Id gameId);

    List<PlayerDto> getPlayers(Game.@NonNull Id gameId);

    List<CityDto> getCities(Game.@NonNull Id gameId);
}
