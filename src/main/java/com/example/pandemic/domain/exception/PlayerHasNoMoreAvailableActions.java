package com.example.pandemic.domain.exception;

public class PlayerHasNoMoreAvailableActions extends DomainException {
  public PlayerHasNoMoreAvailableActions(String message) {
    super(message);
  }
}
