package com.example.pandemic.domain.exception;

public class DiseaseIsNotCured extends DomainException {
  public DiseaseIsNotCured(String message) {
    super(message);
  }
}
