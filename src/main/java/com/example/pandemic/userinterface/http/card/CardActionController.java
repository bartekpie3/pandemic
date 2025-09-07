package com.example.pandemic.userinterface.http.card;

import com.example.pandemic.application.GameCommandService;
import com.example.pandemic.domain.Game;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games/{gameId}/cards/")
@RequiredArgsConstructor
public class CardActionController {

  private final GameCommandService gameService;

  @PostMapping("discard")
  public ResponseEntity<ActionResponse> discardCard(
      @PathVariable UUID gameId, @Valid @RequestBody DiscardCardRequest request) {
    var result =
        gameService.discardCard(Game.Id.from(gameId), request.playerIndex(), request.cardName());

    if (result.isSuccess()) {
      return ResponseEntity.ok(new ActionResponse(result.value()));
    }

    return ResponseEntity.badRequest().body(new ActionResponse(result.error()));
  }

  @PostMapping("play-event")
  public ResponseEntity<ActionResponse> playEventCard(
      @PathVariable UUID gameId, @Valid @RequestBody PlayEventCardRequest request) {
    var result =
        gameService.playEventCard(Game.Id.from(gameId), request.playerIndex(), request.cardName());

    if (result.isSuccess()) {
      return ResponseEntity.ok(new ActionResponse(result.value()));
    }

    return ResponseEntity.badRequest().body(new ActionResponse(result.error()));
  }

  public record ActionResponse(String message) {}
}
