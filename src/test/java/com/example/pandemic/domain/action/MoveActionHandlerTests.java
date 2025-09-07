package com.example.pandemic.domain.action;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.action.MoveAction;
import com.example.pandemic.domain.action.move.MoveType;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.support.GameBuilder;
import com.example.pandemic.support.PlayerBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MoveActionHandlerTests {

  private final MoveActionHandler moveActionHandler = new MoveActionHandler();

  @Test
  public void shouldMoveToConnectedCityByDrive() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new MoveAction(City.Name.MIAMI, MoveType.DRIVE);

    var result = moveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getActivePlayer().getCurrentLocation()).isEqualTo(City.Name.MIAMI);
  }

  @Test
  public void shouldNotMoveToNotConnectedCityByDrive() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new MoveAction(City.Name.BAGHDAD, MoveType.DRIVE);

    var result = moveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
    assertThat(game.getActivePlayer().getCurrentLocation()).isEqualTo(City.Name.ATLANTA);
  }

  @Test
  public void shouldMoveToCityWithResearchStationByShuttleFlight() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new MoveAction(City.Name.BAGHDAD, MoveType.SHUTTLE_FLIGHT);

    game.cities().get(City.Name.BAGHDAD).buildResearchStation();

    var result = moveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getActivePlayer().getCurrentLocation()).isEqualTo(City.Name.BAGHDAD);
  }

  @Test
  public void shouldNotMoveToCityWithoutResearchStationByShuttleFlight() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new MoveAction(City.Name.BAGHDAD, MoveType.SHUTTLE_FLIGHT);

    var result = moveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
  }

  @Test
  public void shouldNotMoveFromCityWithoutResearchStationByShuttleFlight() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new MoveAction(City.Name.BAGHDAD, MoveType.SHUTTLE_FLIGHT);

    game.cities().get(City.Name.ATLANTA).removeResearchStation();
    game.cities().get(City.Name.BAGHDAD).buildResearchStation();

    var result = moveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
  }

  @Test
  public void shouldNotMoveWhenBothCitiesDoNotHaveResearchStationByShuttleFlight() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new MoveAction(City.Name.BAGHDAD, MoveType.SHUTTLE_FLIGHT);

    game.cities().get(City.Name.ATLANTA).removeResearchStation();

    var result = moveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
  }

  @Test
  public void shouldMoveToCityByCharterFlightWhenPlayerHasCurrentLocationCityCard() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.ATLANTA)))
                    .build())
            .build();
    var actionRequest = new MoveAction(City.Name.BAGHDAD, MoveType.CHARTER_FLIGHT);

    var result = moveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getActivePlayer().hasCityCard(City.Name.ATLANTA)).isFalse();
    assertThat(game.getActivePlayer().getCurrentLocation()).isEqualTo(City.Name.BAGHDAD);
  }

  @Test
  public void shouldNotMoveToCityByCharterFlightWhenPlayerHasNotCurrentLocationCityCard() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new MoveAction(City.Name.BAGHDAD, MoveType.CHARTER_FLIGHT);

    var result = moveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
  }

  @Test
  public void shouldMoveToCityByDirectFlightWhenPlayerHasDestinationCityCard() {
    var game =
        GameBuilder.aGame()
            .withActivePlayer(
                PlayerBuilder.aPlayer()
                    .withCardsInHand(List.of(PlayerCard.createCityCard(City.Name.BAGHDAD)))
                    .build())
            .build();
    var actionRequest = new MoveAction(City.Name.BAGHDAD, MoveType.DIRECT_FLIGHT);

    var result = moveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isTrue();
    assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(3);
    assertThat(game.getActivePlayer().hasCityCard(City.Name.BAGHDAD)).isFalse();
    assertThat(game.getActivePlayer().getCurrentLocation()).isEqualTo(City.Name.BAGHDAD);
  }

  @Test
  public void shouldNotMoveToCityByDirectFlightWhenPlayerHasNotDestinationCityCard() {
    var game = GameBuilder.aGame().build();
    var actionRequest = new MoveAction(City.Name.BAGHDAD, MoveType.CHARTER_FLIGHT);

    var result = moveActionHandler.handle(game, actionRequest);

    assertThat(result.isSuccess()).isFalse();
  }

  @Test
  public void shouldTreatCuredDiseaseFromDestinationCityWhenMedicMoves() {
    // Given
    var game =
        GameBuilder.aGame().withActivePlayer(PlayerBuilder.aPlayer().asMedic().build()).build();
    var actionRequest = new MoveAction(City.Name.MIAMI, MoveType.DRIVE);
    var destinationCity = game.cities().get(City.Name.MIAMI);

    game.discoverCure(Disease.Color.BLUE);
    game.discoverCure(Disease.Color.BLACK);
    destinationCity.addDisease(Disease.Color.BLUE, 3);
    destinationCity.addDisease(Disease.Color.BLACK, 2);
    destinationCity.addDisease(Disease.Color.YELLOW, 2);

    // When
    var result = moveActionHandler.handle(game, actionRequest);

    // Then
    assertThat(result.isSuccess()).isTrue();
    assertThat(destinationCity.getDiseases().get(Disease.Color.BLUE)).isEqualTo(0);
    assertThat(destinationCity.getDiseases().get(Disease.Color.BLACK)).isEqualTo(0);
    assertThat(destinationCity.getDiseases().get(Disease.Color.YELLOW)).isEqualTo(2);
  }
}
