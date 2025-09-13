package com.example.pandemic.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.application.GameMode;
import com.example.pandemic.domain.model.Player;
import com.example.pandemic.userinterface.http.game.CreateGameController;
import com.example.pandemic.userinterface.http.game.CreateGameRequest;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateGameEndpointTests {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void shouldCreateGameWithSuccess() {
    var request =
        new CreateGameRequest(
            Set.of(Player.Role.MEDIC.name(), Player.Role.RESEARCHER.name()), GameMode.EASY.name());

    var response =
        restTemplate.postForEntity(
            "/games", request, CreateGameController.CreateGameResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getHeaders().getLocation()).isNotNull();
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().gameId()).isNotNull();
  }
}
