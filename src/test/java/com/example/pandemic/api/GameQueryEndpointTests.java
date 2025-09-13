package com.example.pandemic.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.GameCommandService;
import com.example.pandemic.application.GameMode;
import com.example.pandemic.application.query.CityDto;
import com.example.pandemic.application.query.GameDto;
import com.example.pandemic.application.query.PlayerDto;
import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameQueryEndpointTests {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private GameCommandService gameCommandService;

  private UUID gameId;

  @BeforeAll
  void setup() {
    Set<Player.Role> players = new LinkedHashSet<>();
    players.add(Player.Role.MEDIC);
    players.add(Player.Role.RESEARCHER);

    gameId = gameCommandService.createGame(GameMode.EASY, players).value();
  }

  @Test
  public void shouldReturnGameData() {
    // When
    var response = restTemplate.getForEntity("/games/" + gameId, GameDto.class);

    // Then
    var responseBody = response.getBody();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.currentPlayerTurnIndex()).isEqualTo(0);
    assertThat(responseBody.state()).isEqualTo(Game.State.ACTION.name());
    assertThat(responseBody.outbreakMarkerPosition()).isEqualTo(0);
    assertThat(responseBody.infectionRatePosition()).isEqualTo(0);
  }

  @Test
  public void shouldReturnPlayersData() {
    // When
    ResponseEntity<PlayerDto[]> response =
        restTemplate.getForEntity("/games/" + gameId + "/players", PlayerDto[].class);

    // Then
    var responseBody = response.getBody();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody).hasSize(2);
    // first player
    assertThat(responseBody[0].role()).isEqualTo(Player.Role.MEDIC.name());
    assertThat(responseBody[0].numberOfAvailableActions()).isEqualTo(4);
    assertThat(responseBody[0].currentLocation()).isEqualTo(City.STARTING_CITY_NAME.name());
    assertThat(responseBody[0].cardsInHand().size()).isEqualTo(4);
    // second player
    assertThat(responseBody[1].role()).isEqualTo(Player.Role.RESEARCHER.name());
    assertThat(responseBody[1].numberOfAvailableActions()).isEqualTo(0);
    assertThat(responseBody[1].currentLocation()).isEqualTo(City.STARTING_CITY_NAME.name());
    assertThat(responseBody[1].cardsInHand().size()).isEqualTo(4);
  }

  @Test
  public void shouldReturnCitiesData() {
    // When
    ResponseEntity<CityDto[]> response =
        restTemplate.getForEntity("/games/" + gameId + "/cities", CityDto[].class);

    // Then
    var responseBody = response.getBody();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseBody).isNotNull();
    assertThat(responseBody).hasSize(48);
  }
}
