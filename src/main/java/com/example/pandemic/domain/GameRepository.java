package com.example.pandemic.domain;

import com.example.pandemic.domain.exception.GameNotFound;

public interface GameRepository {

  void create(Game game);

  void save(Game game);

  /**
   * @throws GameNotFound
   */
  Game get(Game.Id gameId);
}
