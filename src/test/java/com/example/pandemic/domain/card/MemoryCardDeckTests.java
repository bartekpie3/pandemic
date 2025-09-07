package com.example.pandemic.domain.card;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class MemoryCardDeckTests {

  @Test
  public void shouldDrawCard() {
    var deck = MemoryCardDeck.initPlayerDeck();

    var card = deck.drawCard();

    assertThat(card).isNotNull();
    assertThat(deck.size()).isEqualTo(52);
  }

  @Test
  public void shouldShuffleEvenly() {
    var deck = MemoryCardDeck.initPlayerDeck();

    deck.shuffleEvenly(
        List.of(
            PlayerCard.createEpidemicCard(),
            PlayerCard.createEpidemicCard(),
            PlayerCard.createEpidemicCard(),
            PlayerCard.createEpidemicCard()));

    assertThat(deck.size()).isEqualTo(57);
  }
}
