package com.example.pandemic.domain;

import com.example.pandemic.domain.dto.CityDto;
import com.example.pandemic.domain.dto.GameDto;
import com.example.pandemic.domain.dto.PlayerDto;
import lombok.NonNull;

import java.util.List;

public interface QueryGameRepository {

    GameDto get(Game.@NonNull Id gameId);

    List<PlayerDto> getPlayers(Game.@NonNull Id gameId);

    List<CityDto> getCities(Game.@NonNull Id gameId);
}
