package com.example.pandemic.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.GameCommandService;
import com.example.pandemic.application.GameMode;
import com.example.pandemic.domain.action.move.MoveType;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Player;
import com.example.pandemic.userinterface.http.action.ActionController;
import com.example.pandemic.userinterface.http.action.MoveActionRequest;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameActionEndpointTests {

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
  void shouldPerformActionMove() {
    var request = new MoveActionRequest(City.Name.MIAMI.name(), MoveType.DRIVE.name());

    var response =
        restTemplate.postForEntity(
            "/games/{gameId}/actions/move", request, ActionController.ActionResponse.class, gameId);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().message()).isEqualTo("Player moved to MIAMI");
  }
}
