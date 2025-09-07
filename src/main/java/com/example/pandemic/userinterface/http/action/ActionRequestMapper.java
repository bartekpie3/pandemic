package com.example.pandemic.userinterface.http.action;

import com.example.pandemic.application.action.DiscoverClueAction;
import com.example.pandemic.application.action.DispatcherMovePlayerAction;
import com.example.pandemic.application.action.DispatcherMovePlayerToPlayerAction;
import com.example.pandemic.application.action.MoveAction;
import com.example.pandemic.application.action.OperationsExpertSpecialMoveAction;
import com.example.pandemic.application.action.ShareKnowledgeAction;
import com.example.pandemic.application.action.TreatDiseaseAction;
import com.example.pandemic.domain.action.move.MoveType;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import org.springframework.stereotype.Component;

@Component
class ActionRequestMapper {

  public MoveAction mapRequestToMoveAction(MoveActionRequest request) {
    return new MoveAction(
        City.Name.valueOf(request.destinationCityName()), MoveType.valueOf(request.moveType()));
  }

  public DiscoverClueAction mapRequestToDiscoverClueAction(DiscoverClueActionRequest request) {
    return new DiscoverClueAction(
        request.cardsUsed().stream()
            .map(City.Name::valueOf)
            .collect(java.util.stream.Collectors.toSet()));
  }

  public ShareKnowledgeAction mapRequestToShareKnowledgeAction(
      ShareKnowledgeActionRequest request) {
    return new ShareKnowledgeAction(
        request.playerToSwap(), City.Name.valueOf(request.cityCardName()));
  }

  public TreatDiseaseAction mapRequestToTreatDiseaseAction(TreatDiseaseActionRequest request) {
    return new TreatDiseaseAction(Disease.Color.valueOf(request.disease()));
  }

  public OperationsExpertSpecialMoveAction mapRequestToOperationsExpertSpecialMoveAction(
      OperationsExpertSpecialMoveActionRequest request) {
    return new OperationsExpertSpecialMoveAction(
        City.Name.valueOf(request.destinationCityName()), request.cardName());
  }

  public DispatcherMovePlayerToPlayerAction mapRequestToDispatcherMovePlayerToPlayerAction(
      DispatcherMovePlayerToPlayerActionRequest request) {
    return new DispatcherMovePlayerToPlayerAction(
        request.playerIndexWhichMoves(), request.playerIndexToWhichMoves());
  }

  public DispatcherMovePlayerAction mapRequestToDispatcherMovePlayerAction(
      DispatcherMovePlayerActionRequest request) {
    return new DispatcherMovePlayerAction(
        request.playerIndexWhichMoves(),
        City.Name.valueOf(request.destinationCityName()),
        MoveType.valueOf(request.moveType()));
  }
}
