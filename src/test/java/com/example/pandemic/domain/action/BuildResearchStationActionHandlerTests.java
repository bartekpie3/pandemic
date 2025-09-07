package com.example.pandemic.domain.action;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.action.BuildResearchStationAction;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.support.GameBuilder;
import com.example.pandemic.support.PlayerBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BuildResearchStationActionHandlerTests {

  private final BuildResearchStationActionHandler handler = new BuildResearchStationActionHandler();

  @Test
  public void shouldBuildResearchStationAndRemoveCityCardFromPlayerHand() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .asMedic()
                    .inCity(City.Name.BAGHDAD)
                    .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.BAGHDAD)))
                    .build())
            .build();
    var actionRequest = new BuildResearchStationAction();

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().hasCityCard(City.Name.BAGHDAD)).isFalse();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
  }

  @Test
  public void shouldBuildResearchStationAndKeepCityCardInPlayerHandWhenPlayerIsOperationsExpert() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .asOperationsExpert()
                    .inCity(City.Name.BAGHDAD)
                    .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.BAGHDAD)))
                    .build())
            .build();
    var actionRequest = new BuildResearchStationAction();

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().hasCityCard(City.Name.BAGHDAD)).isTrue();
  }

  @Test
  public void shouldNotBuildResearchStationWhenPlayerDoNotHaveCityCardInHand() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(PlayerBuilder.aPlayer().asMedic().inCity(City.Name.BAGHDAD).build())
            .build();
    var actionRequest = new BuildResearchStationAction();

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
  }

  @Test
  public void shouldNotBuildResearchStationWhenCityAlreadyHasResearchStation() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .asMedic()
                    .inCity(City.Name.BAGHDAD)
                    .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.BAGHDAD)))
                    .build())
            .build();
    var actionRequest = new BuildResearchStationAction();

    game.cities().get(City.Name.BAGHDAD).buildResearchStation();

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
  }
}
