package com.example.pandemic.domain.action;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.action.TreatDiseaseAction;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.support.GameBuilder;
import com.example.pandemic.support.PlayerBuilder;
import org.junit.jupiter.api.Test;

public class TreatDiseaseActionHandlerTests {

  private final TreatDiseaseActionHandler handler = new TreatDiseaseActionHandler();

  @Test
  public void shouldCureOneDisease() {
    var game =
        GameBuilder.aGame().withActivePlayer(PlayerBuilder.aPlayer().asScientist().build()).build();
    var actionRequest = new TreatDiseaseAction(Disease.Color.BLACK);

    game.cities().get(City.Name.ATLANTA).addDisease(Disease.Color.BLACK, 3);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.cities().get(City.Name.ATLANTA).getDiseases().get(Disease.Color.BLACK))
        .isEqualTo(2);
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.isDiseaseEradicated(Disease.Color.BLACK)).isFalse();
  }

  @Test
  public void shouldCureAllDiseaseWhenPlayerIsMedic() {
    var game =
        GameBuilder.aGame().withActivePlayer(PlayerBuilder.aPlayer().asMedic().build()).build();
    var actionRequest = new TreatDiseaseAction(Disease.Color.BLACK);

    game.cities().get(City.Name.ATLANTA).addDisease(Disease.Color.BLACK, 3);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.cities().get(City.Name.ATLANTA).getDiseases().get(Disease.Color.BLACK))
        .isEqualTo(0);
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.isDiseaseEradicated(Disease.Color.BLACK)).isFalse();
  }

  @Test
  public void shouldCureAllDiseaseWhenDiseaseIsCured() {
    var game =
        GameBuilder.aGame().withActivePlayer(PlayerBuilder.aPlayer().asScientist().build()).build();
    var actionRequest = new TreatDiseaseAction(Disease.Color.BLACK);

    game.cureDisease(Disease.Color.BLACK);
    game.cities().get(City.Name.ATLANTA).addDisease(Disease.Color.BLACK, 3);

    var result = handler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.cities().get(City.Name.ATLANTA).getDiseases().get(Disease.Color.BLACK))
        .isEqualTo(0);
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.isDiseaseEradicated(Disease.Color.BLACK)).isTrue();
  }
}
