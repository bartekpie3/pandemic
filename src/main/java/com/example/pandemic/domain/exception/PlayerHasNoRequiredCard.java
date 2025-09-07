package com.example.pandemic.domain.exception;

public class PlayerHasNoRequiredCard extends DomainException {
  public PlayerHasNoRequiredCard(String message) {
    super(message);
  }
}
