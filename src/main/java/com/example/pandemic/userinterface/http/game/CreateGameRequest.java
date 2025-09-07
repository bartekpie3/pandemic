package com.example.pandemic.userinterface.http.game;

import com.example.common.validation.ValidEnum;
import com.example.pandemic.application.GameMode;
import com.example.pandemic.domain.model.Player;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record CreateGameRequest(
    @Size(min = 2, max = 4, message = "Number of players must be between 2 and 4")
        @ValidEnum(enumClass = Player.Role.class)
        @NotNull
        Set<String> players,
    @ValidEnum(enumClass = GameMode.class, message = "GameMode must be EASY, NORMAL or HARD")
        @NotNull
        String gameMode) {}
