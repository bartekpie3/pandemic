package com.example.pandemic.domain.action.request;

import com.example.pandemic.domain.model.Disease;
import lombok.NonNull;

public interface TreatDiseaseActionRequest extends PlayerActionRequest {

  @NonNull
  Disease.Color disease();
}
