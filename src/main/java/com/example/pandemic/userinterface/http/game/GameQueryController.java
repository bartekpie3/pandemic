package com.example.pandemic.userinterface.http.game;

import com.example.pandemic.application.GameQueryService;
import com.example.pandemic.application.query.CityDto;
import com.example.pandemic.application.query.GameDto;
import com.example.pandemic.application.query.PlayerDto;
import com.example.pandemic.domain.Game;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games/{gameId}")
public class GameQueryController {

  private final GameQueryService gameQueryService;

  @GetMapping()
  public ResponseEntity<GameDto> getGame(@PathVariable UUID gameId) {
    var game = gameQueryService.getGame(Game.Id.from(gameId));

    return ResponseEntity.ok(game);
  }

  @GetMapping("/players")
  public ResponseEntity<List<PlayerDto>> getPlayers(@PathVariable UUID gameId) {
    var players = gameQueryService.getPlayers(Game.Id.from(gameId));

    return ResponseEntity.ok(players);
  }

  @GetMapping("/cities")
  public ResponseEntity<List<CityDto>> getCities(@PathVariable UUID gameId) {
    var players = gameQueryService.getCities(Game.Id.from(gameId));

    return ResponseEntity.ok(players);
  }
}
