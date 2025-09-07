package com.example.pandemic.infrastructure.repository;

import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.GameRepository;
import com.example.pandemic.domain.exception.GameNotFound;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("dev")
@Repository
public class InMemoryGameRepository implements GameRepository {

  private final Map<Game.Id, Game> games = new HashMap<>();

  @Override
  public void create(Game game) {
    games.put(game.getId(), game);
  }

  @Override
  public void save(Game game) {
    games.put(game.getId(), game);
  }

  @Override
  public Game get(Game.Id gameId) {
    return Optional.ofNullable(games.get(gameId))
        .orElseThrow(() -> new GameNotFound("Game not found: " + gameId));
  }
}
