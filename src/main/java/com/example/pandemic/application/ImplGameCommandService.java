package com.example.pandemic.application;

import com.example.common.Result;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.GameRepository;
import com.example.pandemic.domain.action.request.ActionRequest;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.card.PlayerCardEventName;
import com.example.pandemic.domain.model.Player;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class ImplGameCommandService implements GameCommandService {

  private final GameRepository gameRepository;

  @Transactional
  @Override
  public Game.Id createGame(@NonNull GameMode gameMode, @NonNull Set<Player.Role> playersRole) {
    var game = new CreateGameHelper().create(gameMode, playersRole);

    gameRepository.create(game);

    log.info("Game {} created (mode={}, players={})", game.getId(), gameMode, playersRole);

    return game.getId();
  }

  @Transactional
  @Override
  public Result<String, String> performAction(
      Game.@NonNull Id gameId, @NonNull ActionRequest actionRequest) {
    var game = gameRepository.get(gameId);

    var result = game.performAction(actionRequest);

    gameRepository.save(game);

    return result;
  }

  @Transactional
  @Override
  public Result<String, String> discardCard(
      Game.@NonNull Id gameId, int playerIndex, @NonNull String cardName) {
    var game = gameRepository.get(gameId);
    var player = game.getPlayer(playerIndex);
    var card = PlayerCard.tryFromName(cardName);

    if (!player.hasCard(card)) {
      return new Result.Failure<>("Player has no required card - " + cardName);
    }

    player.discardCard(card);

    gameRepository.save(game);

    log.info("Player {} discarded card {}", player, card);

    return new Result.Success<>("Player discarded card");
  }

  @Transactional
  @Override
  public Result<String, String> playEventCard(
      Game.@NonNull Id gameId, int playerIndex, @NonNull String cardName) {
    var game = gameRepository.get(gameId);
    var player = game.getPlayer(playerIndex);
    var card = PlayerCard.createEventCard(PlayerCardEventName.valueOf(cardName));

    if (!player.hasCard(card)) {
      return new Result.Failure<>("Player has no required card - " + cardName);
    }

    // todo

    player.discardCard(card);
    game.getPlayerDiscardPile().addCard(card);

    gameRepository.save(game);

    log.info("Player {} played event card {}", player, cardName);

    return new Result.Success<>("Player played event card - " + cardName);
  }
}
