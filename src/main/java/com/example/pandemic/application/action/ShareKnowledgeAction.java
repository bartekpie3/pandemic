package com.example.pandemic.application.action;

import com.example.pandemic.domain.action.request.ShareKnowledgeActionRequest;
import com.example.pandemic.domain.model.City;
import lombok.NonNull;

public record ShareKnowledgeAction(int playerToSwap, City.@NonNull Name cityCardName)
    implements ShareKnowledgeActionRequest {}
