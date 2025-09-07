package com.example.pandemic.domain.action;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.action.ShareKnowledgeAction;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.support.GameBuilder;
import com.example.pandemic.support.PlayerBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

// more details tests in swap service tests
public class ShareKnowledgeActionHandlerTests {

  private final ShareKnowledgeActionHandler handler = new ShareKnowledgeActionHandler();

  @Test
  public void shouldShareKnowledgeSuccessAndRemoveCityCardFromPlayerHand() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.ATLANTA)))
                    .build())
            .build();
    var actionRequest = new ShareKnowledgeAction(1, City.Name.ATLANTA);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getActivePlayer().hasCityCard(City.Name.ATLANTA)).isFalse();
    assertThat(game.getPlayer(1).hasCityCard(City.Name.ATLANTA)).isTrue();
  }

  @Test
  public void shouldNotShareKnowledgeWhenPlayerDoNotHaveCityCardInHand() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new ShareKnowledgeAction(1, City.Name.ATLANTA);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
  }
}
