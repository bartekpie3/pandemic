package com.example.pandemic.domain.exception;

import com.example.common.NotFoundException;

public class GameNotFound extends NotFoundException {
  public GameNotFound(String message) {
    super(message);
  }
}
