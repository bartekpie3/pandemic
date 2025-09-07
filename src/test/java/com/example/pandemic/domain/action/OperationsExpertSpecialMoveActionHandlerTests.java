package com.example.pandemic.domain.action;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.action.OperationsExpertSpecialMoveAction;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.support.GameBuilder;
import com.example.pandemic.support.PlayerBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OperationsExpertSpecialMoveActionHandlerTests {

  private final OperationsExpertSpecialMoveActionHandler operationsExpertSpecialMoveActionHandler =
      new OperationsExpertSpecialMoveActionHandler();

  @Test
  void shouldHandleOperationsExpertSpecialMoveAction() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .asOperationsExpert()
                    .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.WASHINGTON)))
                    .build())
            .build();
    var actionRequest =
        new OperationsExpertSpecialMoveAction(City.Name.BAGHDAD, City.Name.WASHINGTON.name());

    var result = operationsExpertSpecialMoveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getActivePlayer().getCurrentLocation()).isEqualTo(City.Name.BAGHDAD);
    assertThat(game.getActivePlayer().hasCityCard(City.Name.WASHINGTON)).isFalse();
    assertThat(game.getActivePlayer().isHasSpecialActionUsed()).isTrue();
  }

  @Test
  void shouldFailHandleWhenPlayerIsNotOperationsExpert() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .asMedic()
                    .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.WASHINGTON)))
                    .build())
            .build();
    var actionRequest =
        new OperationsExpertSpecialMoveAction(City.Name.BAGHDAD, City.Name.WASHINGTON.name());

    var result = operationsExpertSpecialMoveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Player is not Operations expert");
  }

  @Test
  void shouldFailHandleWhenPlayerAlreadyHasSpecialActionUsed() {
    var player =
        PlayerBuilder.aPlayer()
            .asOperationsExpert()
            .withUsedSpecialAction()
            .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.WASHINGTON)))
            .build();
    var game = GameBuilder.aGame().withActivePlayer(player).build();
    var actionRequest =
        new OperationsExpertSpecialMoveAction(City.Name.BAGHDAD, City.Name.WASHINGTON.name());
    player.useSpecialAction();

    var result = operationsExpertSpecialMoveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Player has already used special action");
  }

  @Test
  void shouldFailHandleWhenPlayerLocationHasNoResearchStation() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .asOperationsExpert()
                    .inCity(City.Name.MADRID)
                    .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.WASHINGTON)))
                    .build())
            .build();
    var actionRequest =
        new OperationsExpertSpecialMoveAction(City.Name.BAGHDAD, City.Name.WASHINGTON.name());

    var result = operationsExpertSpecialMoveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Player is not in a research station");
  }

  @Test
  void shouldFailHandleWhenPlayerHasNoUsedCardInHand() {
    var player = PlayerBuilder.aPlayer().asOperationsExpert().build();
    var game = GameBuilder.aGame().withActivePlayer(player).build();
    var actionRequest =
        new OperationsExpertSpecialMoveAction(City.Name.BAGHDAD, City.Name.WASHINGTON.name());

    var result = operationsExpertSpecialMoveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.error()).isEqualTo("Player has no used card - WASHINGTON");
  }
}
