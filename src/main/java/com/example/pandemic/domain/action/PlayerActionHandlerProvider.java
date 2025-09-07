package com.example.pandemic.domain.action;

import com.example.pandemic.domain.action.request.*;

public final class PlayerActionHandlerProvider {

  private PlayerActionHandlerProvider() {}

  @SuppressWarnings("unchecked")
  public static <T extends ActionRequest> ActionHandler<T> getActionHandler(T actionRequest) {
    if (actionRequest instanceof MoveActionRequest) {
      return (ActionHandler<T>) new MoveActionHandler();
    }

    if (actionRequest instanceof DiscoverClueActionRequest) {
      return (ActionHandler<T>) new DiscoverCureActionHandler();
    }

    if (actionRequest instanceof TreatDiseaseActionRequest) {
      return (ActionHandler<T>) new TreatDiseaseActionHandler();
    }

    if (actionRequest instanceof BuildResearchStationActionRequest) {
      return (ActionHandler<T>) new BuildResearchStationActionHandler();
    }

    if (actionRequest instanceof ShareKnowledgeActionRequest) {
      return (ActionHandler<T>) new ShareKnowledgeActionHandler();
    }

    if (actionRequest instanceof PassActionRequest) {
      return (ActionHandler<T>) new PassActionHandler();
    }

    if (actionRequest instanceof OperationsExpertSpecialMoveActionHandler) {
      return (ActionHandler<T>) new OperationsExpertSpecialMoveActionHandler();
    }

    if (actionRequest instanceof DispatcherMovePlayerToPlayerActionRequest) {
      return (ActionHandler<T>) new DispatcherMovePlayerToPlayerActionHandler();
    }

    if (actionRequest instanceof DispatcherMovePlayerActionRequest) {
      return (ActionHandler<T>) new DispatcherMovePlayerActionHandler();
    }

    throw new IllegalArgumentException("Unknown action type");
  }
}
