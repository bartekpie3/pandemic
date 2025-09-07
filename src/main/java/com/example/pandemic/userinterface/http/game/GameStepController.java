package com.example.pandemic.userinterface.http.game;

import com.example.pandemic.application.GameCommandService;
import com.example.pandemic.application.action.DrawStepAction;
import com.example.pandemic.application.action.InfectStepAction;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.ActionRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games/{gameId}/steps")
public class GameStepController {

  private final GameCommandService gameCommandService;

  @PostMapping("/draw-card")
  public ResponseEntity<StepResponse> drawCard(@PathVariable UUID gameId) {
    return performStep(gameId, new DrawStepAction());
  }

  @PostMapping("/infect-cities")
  public ResponseEntity<StepResponse> infectCities(@PathVariable UUID gameId) {
    return performStep(gameId, new InfectStepAction());
  }

  private ResponseEntity<StepResponse> performStep(UUID gameId, ActionRequest action) {
    var result = gameCommandService.performAction(Game.Id.from(gameId), action);

    if (result.isSuccess()) {
      return ResponseEntity.ok(new StepResponse(result.value()));
    }

    return ResponseEntity.badRequest().body(new StepResponse(result.error()));
  }

  public record StepResponse(String message) {}
}
