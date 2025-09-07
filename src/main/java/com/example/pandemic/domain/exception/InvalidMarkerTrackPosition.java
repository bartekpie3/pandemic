package com.example.pandemic.domain.exception;

public class InvalidMarkerTrackPosition extends DomainException {

  private InvalidMarkerTrackPosition(String message) {
    super(message);
  }

  public static InvalidMarkerTrackPosition invalidInitPosition() {
    return new InvalidMarkerTrackPosition("Initial position must be 0");
  }

  public static InvalidMarkerTrackPosition outOfRangePosition() {
    return new InvalidMarkerTrackPosition("Cannot move marker to position greater than 6");
  }
}
