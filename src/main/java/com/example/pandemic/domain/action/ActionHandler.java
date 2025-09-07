package com.example.pandemic.domain.action;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.ActionRequest;

public interface ActionHandler<T extends ActionRequest> {

  Result<String, String> handle(Game game, T action);
}
