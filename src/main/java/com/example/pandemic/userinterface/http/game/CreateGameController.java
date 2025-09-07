package com.example.pandemic.userinterface.http.game;

import com.example.pandemic.application.GameCommandService;
import com.example.pandemic.application.GameMode;
import com.example.pandemic.domain.model.Player;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
public class CreateGameController {

  private final GameCommandService gameCommandService;

  @PostMapping
  public ResponseEntity<CreateGameResponse> createGame(
      @Valid @RequestBody CreateGameRequest request) {
    var gameId =
        gameCommandService
            .createGame(
                GameMode.valueOf(request.gameMode()),
                request.players().stream().map(Player.Role::valueOf).collect(Collectors.toSet()))
            .value();

    var location = URI.create("/games/" + gameId);

    return ResponseEntity.created(location).body(new CreateGameResponse(gameId));
  }

  public record CreateGameResponse(UUID gameId) {}
}
