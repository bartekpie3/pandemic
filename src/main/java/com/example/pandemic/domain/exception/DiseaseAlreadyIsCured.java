package com.example.pandemic.domain.exception;

public class DiseaseAlreadyIsCured extends DomainException {
  public DiseaseAlreadyIsCured(String message) {
    super(message);
  }
}
