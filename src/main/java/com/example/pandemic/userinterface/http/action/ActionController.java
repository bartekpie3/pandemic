package com.example.pandemic.userinterface.http.action;

import com.example.pandemic.application.GameCommandService;
import com.example.pandemic.application.action.BuildResearchStationAction;
import com.example.pandemic.application.action.PassAction;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.ActionRequest;
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
@RequiredArgsConstructor
@RequestMapping("/games/{gameId}/actions/")
public class ActionController {

  private final GameCommandService gameService;

  private final ActionRequestMapper actionMapper;

  @PostMapping("move")
  public ResponseEntity<ActionResponse> moveAction(
      @PathVariable UUID gameId, @RequestBody MoveActionRequest request) {
    return performAction(gameId, actionMapper.mapRequestToMoveAction(request));
  }

  @PostMapping("build-research-station")
  public ResponseEntity<ActionResponse> buildResearchStationAction(@PathVariable UUID gameId) {
    return performAction(gameId, new BuildResearchStationAction());
  }

  @PostMapping("discover-clue")
  public ResponseEntity<ActionResponse> discoverClueAction(
      @PathVariable UUID gameId, @Valid @RequestBody DiscoverClueActionRequest request) {
    return performAction(gameId, actionMapper.mapRequestToDiscoverClueAction(request));
  }

  @PostMapping("share-knowledge")
  public ResponseEntity<ActionResponse> shareKnowledgeAction(
      @PathVariable UUID gameId, @Valid @RequestBody ShareKnowledgeActionRequest request) {
    return performAction(gameId, actionMapper.mapRequestToShareKnowledgeAction(request));
  }

  @PostMapping("treat-disease")
  public ResponseEntity<ActionResponse> treatDiseaseAction(
      @PathVariable UUID gameId, @Valid @RequestBody TreatDiseaseActionRequest request) {
    return performAction(gameId, actionMapper.mapRequestToTreatDiseaseAction(request));
  }

  @PostMapping("pass")
  public ResponseEntity<ActionResponse> passAction(@PathVariable UUID gameId) {
    return performAction(gameId, new PassAction());
  }

  @PostMapping("operations-expert-special-move")
  public ResponseEntity<ActionResponse> operationsExpertSpecialMoveAction(
      @PathVariable UUID gameId, @RequestBody OperationsExpertSpecialMoveActionRequest request) {
    return performAction(
        gameId, actionMapper.mapRequestToOperationsExpertSpecialMoveAction(request));
  }

  @PostMapping("dispatched-move-player-to-player")
  public ResponseEntity<ActionResponse> dispatchedMovePlayerToPlayerAction(
      @PathVariable UUID gameId, @RequestBody DispatcherMovePlayerToPlayerActionRequest request) {
    return performAction(
        gameId, actionMapper.mapRequestToDispatcherMovePlayerToPlayerAction(request));
  }

  @PostMapping("dispatched-move-player")
  public ResponseEntity<ActionResponse> dispatchedMovePlayerAction(
      @PathVariable UUID gameId, @RequestBody DispatcherMovePlayerActionRequest request) {
    return performAction(gameId, actionMapper.mapRequestToDispatcherMovePlayerAction(request));
  }

  private ResponseEntity<ActionResponse> performAction(UUID gameId, ActionRequest action) {
    var result = gameService.performAction(Game.Id.from(gameId), action);

    if (result.isSuccess()) {
      return ResponseEntity.ok(new ActionResponse(result.value()));
    }

    return ResponseEntity.badRequest().body(new ActionResponse(result.error()));
  }

  public record ActionResponse(String message) {}
}
