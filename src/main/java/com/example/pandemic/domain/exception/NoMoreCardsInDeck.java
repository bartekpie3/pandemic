package com.example.pandemic.domain.exception;

public class NoMoreCardsInDeck extends DomainException {
  public NoMoreCardsInDeck(String message) {
    super(message);
  }
}
