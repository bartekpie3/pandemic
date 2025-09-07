package com.example.pandemic.application;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.action.request.ActionRequest;
import com.example.pandemic.domain.model.Player;
import java.util.Set;
import lombok.NonNull;

public interface GameCommandService {

  Game.Id createGame(@NonNull GameMode gameMode, @NonNull Set<Player.Role> playersRole);

  Result<String, String> performAction(
      Game.@NonNull Id gameId, @NonNull ActionRequest actionRequest);

  Result<String, String> discardCard(
      Game.@NonNull Id gameId, int playerIndex, @NonNull String cardName);

  Result<String, String> playEventCard(
      Game.@NonNull Id gameId, int playerIndex, @NonNull String cardName);
}
