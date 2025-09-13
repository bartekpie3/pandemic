package com.example.pandemic.domain.card;

import java.util.ArrayList;
import java.util.Collections;

public final class DeckHelper {

  private DeckHelper() {}

  public static <T extends Card> void shuffleGivenDeckOnTopOfTheDeck(
      CardDeck<T> deck, CardDeck<T> deckToShuffle) {
    var cards = new ArrayList<T>(deckToShuffle.size());

    for (var i = 0; i <= deckToShuffle.size(); i++) {
      cards.add(deckToShuffle.drawCard());
    }

    Collections.shuffle(cards);

    cards.forEach(deck::addCard);
  }
}
