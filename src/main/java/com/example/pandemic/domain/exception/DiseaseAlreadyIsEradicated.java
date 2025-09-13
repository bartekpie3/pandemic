package com.example.pandemic.domain.exception;

public class DiseaseAlreadyIsEradicated extends DomainException {
  public DiseaseAlreadyIsEradicated(String message) {
    super(message);
  }
}
