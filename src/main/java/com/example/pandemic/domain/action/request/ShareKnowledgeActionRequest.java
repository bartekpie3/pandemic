package com.example.pandemic.domain.action.request;

import com.example.pandemic.domain.model.City;
import lombok.NonNull;

public interface ShareKnowledgeActionRequest extends PlayerActionRequest {

    int playerToSwap();

    @NonNull
    City.Name cityCardName();
}
