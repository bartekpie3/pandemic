package com.example.pandemic.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.support.PlayerBuilder;
import org.junit.jupiter.api.Test;

public class PlayerTests {

  @Test
  public void shouldReturnCityCardFromPlayerHand() {
    var player = PlayerBuilder.aPlayer().build();
    var card = PlayerCard.createCityCard(City.Name.SAN_FRANCISCO);

    player.addCard(card);

    var playerCard = player.getCityCard(City.Name.SAN_FRANCISCO);

    assertThat(playerCard).isSameAs(card);
  }
}
