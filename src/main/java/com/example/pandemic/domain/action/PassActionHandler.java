package com.example.pandemic.domain.action;

import com.example.common.Result;
import com.example.pandemic.domain.action.request.PassActionRequest;
import com.example.pandemic.domain.Game;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class PassActionHandler implements ActionHandler<PassActionRequest> {

  @Override
  public Result<String, String> handle(Game game, PassActionRequest action) {
    var activePlayer = game.getActivePlayer();

    activePlayer.pass();

    log.info("Player {} passed", activePlayer);

    return new Result.Success<>("Player passed");
  }
}
