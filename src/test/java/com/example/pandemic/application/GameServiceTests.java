package com.example.pandemic.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.GameRepository;
import com.example.pandemic.domain.model.Player;
import com.example.pandemic.infrastructure.repository.InMemoryGameRepository;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class GameServiceTests {

  private final GameRepository gameRepository = new InMemoryGameRepository();

  private final ImplGameCommandService gameService = new ImplGameCommandService(gameRepository);

  @Test
  public void shouldCreateGameWithSuccess() {
    var gameId =
        gameService.createGame(GameMode.EASY, Set.of(Player.Role.MEDIC, Player.Role.RESEARCHER));

    var game = gameRepository.get(gameId);

    assertThat(game.getState()).isEqualTo(Game.State.ACTION.name());
    assertThat(game.getInfectionDeck().size()).isEqualTo(39);
    assertThat(game.getPlayerDeck().size()).isEqualTo(49);
  }
}
