package com.example.pandemic.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.domain.GameRepository;
import com.example.pandemic.support.GameBuilder;
import com.example.pandemic.userinterface.http.game.GameStepController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameStepEndpointTests {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private GameRepository gameRepository;

  @Test
  void shouldPerformDrawStep() {
    var game = GameBuilder.aGame().onFirstDrawState().build();

    gameRepository.create(game);

    var response =
        restTemplate.postForEntity(
            "/games/{gameId}/steps/draw-card",
            null,
            GameStepController.StepResponse.class,
            game.getId().value());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void shouldPerformInfectionStep() {
    var game = GameBuilder.aGame().onInfectState().build();

    gameRepository.create(game);

    var response =
        restTemplate.postForEntity(
            "/games/{gameId}/steps/infect-cities",
            null,
            GameStepController.StepResponse.class,
            game.getId().value());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().message()).isEqualTo("Infected cities step executed");
  }
}
