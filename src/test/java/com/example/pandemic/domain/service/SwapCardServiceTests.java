package com.example.pandemic.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.support.PlayerBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SwapCardServiceTests {

  @Test
  public void shouldNotSwapCardWhenPlayersAreNotInSameCity() {
    var cardToSwap = City.Name.SAN_FRANCISCO;
    var firstPlayer = PlayerBuilder.aPlayer().asScientist().build();
    var secondPlayer = PlayerBuilder.aPlayer().asMedic().inCity(City.Name.SAN_FRANCISCO).build();

    var result = SwapCardService.swap(firstPlayer, secondPlayer, cardToSwap);

    assertThat(result.isSuccess()).isFalse();
  }

  @Test
  public void shouldNotSwapCardWhenPlayersAreSamePlayer() {
    var cardToSwap = City.Name.SAN_FRANCISCO;
    var player = PlayerBuilder.aPlayer().build();

    var result = SwapCardService.swap(player, player, cardToSwap);

    assertThat(result.isSuccess()).isFalse();
  }

  @Test
  public void shouldNotSwapCardWhenCardToSwapIsDifferentThanPlayersLocation() {
    var cardToSwap = City.Name.SAN_FRANCISCO;
    var firstPlayer = PlayerBuilder.aPlayer().build();
    var secondPlayer = PlayerBuilder.aPlayer().asScientist().build();

    var result = SwapCardService.swap(firstPlayer, secondPlayer, cardToSwap);

    assertThat(result.isSuccess()).isFalse();
  }

  @Test
  public void shouldSwapCardDifferentThanPlayersLocationWhenActivePlayerIsResearcher() {
    var cardToSwap = City.Name.SAN_FRANCISCO;
    var firstPlayer =
        PlayerBuilder.aPlayer()
            .asResearcher()
            .withCardsInHand(List.of(PlayerCard.createCityCard(cardToSwap)))
            .build();
    var secondPlayer = PlayerBuilder.aPlayer().build();

    var result = SwapCardService.swap(firstPlayer, secondPlayer, cardToSwap);

    assertThat(result.isSuccess()).isTrue();
    assertThat(secondPlayer.hasCityCard(cardToSwap)).isTrue();
    assertThat(firstPlayer.hasCityCard(cardToSwap)).isFalse();
  }

  @Test
  public void shouldSwapCardDifferentThanPlayersLocationWhenSecondPlayerIsResearcher() {
    var cardToSwap = City.Name.SAN_FRANCISCO;
    var firstPlayer = PlayerBuilder.aPlayer().build();

    var secondPlayer =
        PlayerBuilder.aPlayer()
            .asResearcher()
            .withCardsInHand(List.of(PlayerCard.createCityCard(cardToSwap)))
            .build();

    var result = SwapCardService.swap(firstPlayer, secondPlayer, cardToSwap);

    assertThat(result.isSuccess()).isTrue();
    assertThat(secondPlayer.hasCityCard(cardToSwap)).isFalse();
    assertThat(firstPlayer.hasCityCard(cardToSwap)).isTrue();
  }

  @Test
  public void shouldSwapCardWhenCardToSwapIsSameAsPlayersLocationAndFirstPlayerGotThisCard() {
    var cardToSwap = City.Name.ATLANTA;
    var firstPlayer =
        PlayerBuilder.aPlayer()
            .withCardsInHand(List.of(PlayerCard.createCityCard(cardToSwap)))
            .asScientist()
            .build();
    var secondPlayer = PlayerBuilder.aPlayer().build();

    var result = SwapCardService.swap(firstPlayer, secondPlayer, cardToSwap);

    assertThat(result.isSuccess()).isTrue();
    assertThat(secondPlayer.hasCityCard(cardToSwap)).isTrue();
    assertThat(firstPlayer.hasCityCard(cardToSwap)).isFalse();
  }

  @Test
  public void shouldSwapCardWhenCardToSwapIsSameAsPlayersLocationAndSecondPlayerGotThisCard() {
    var cardToSwap = City.Name.ATLANTA;
    var firstPlayer = PlayerBuilder.aPlayer().asScientist().build();
    var secondPlayer =
        PlayerBuilder.aPlayer()
            .withCardsInHand(List.of(PlayerCard.createCityCard(cardToSwap)))
            .build();

    var result = SwapCardService.swap(firstPlayer, secondPlayer, cardToSwap);

    assertThat(result.isSuccess()).isTrue();
    assertThat(secondPlayer.hasCityCard(cardToSwap)).isFalse();
    assertThat(firstPlayer.hasCityCard(cardToSwap)).isTrue();
  }

  @Test
  public void shouldReturnFalseWhenPlayersNotHaveCard() {
    var cardToSwap = City.Name.ATLANTA;
    var firstPlayer = PlayerBuilder.aPlayer().asScientist().build();
    var secondPlayer = PlayerBuilder.aPlayer().build();

    var result = SwapCardService.swap(firstPlayer, secondPlayer, cardToSwap);

    assertThat(result.isSuccess()).isFalse();
  }
}
