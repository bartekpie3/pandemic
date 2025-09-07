package com.example.pandemic.domain.action;

import com.example.common.Result;
import com.example.pandemic.domain.action.request.ShareKnowledgeActionRequest;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.service.SwapCardService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class ShareKnowledgeActionHandler implements ActionHandler<ShareKnowledgeActionRequest> {

  @Override
  public Result<String, String> handle(Game game, ShareKnowledgeActionRequest action) {
    var activePlayer = game.getActivePlayer();
    var secondPlayer = game.getPlayer(action.playerToSwap());

    var swapResult = SwapCardService.swap(activePlayer, secondPlayer, action.cityCardName());

    if (!swapResult.isSuccess()) {
      return swapResult;
    }

    activePlayer.takeAction();

    log.info(
        "Player {} swapped card {} with {}",
        game.getActivePlayer(),
        action.cityCardName(),
        secondPlayer);

    return new Result.Success<>("Players swapped city card");
  }
}
