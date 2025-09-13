package com.example.pandemic.domain;

import com.example.pandemic.domain.exception.GameNotFound;
import com.example.pandemic.domain.model.Player;

public interface GameRepository {

  void create(Game game);

  void save(Game game);

  void savePlayer(Player player);

  /**
   * @throws GameNotFound
   */
  Game get(Game.Id gameId);
}
