package com.example.pandemic.domain.exception;

public class InvalidGameState extends DomainException {
  public InvalidGameState(String message) {
    super(message);
  }
}
