package com.example.pandemic.domain.card;

import com.example.pandemic.domain.exception.NoMoreCardsInDeck;
import com.example.pandemic.domain.model.City;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record MemoryCardDeck<T extends Card>(List<T> deck) implements CardDeck<T> {

  public static <T extends Card> MemoryCardDeck<T> init() {
    return new MemoryCardDeck<>(new ArrayList<>());
  }

  public static MemoryCardDeck<PlayerCard> initPlayerDeck() {
    var estimatedSize = City.Name.values().length + PlayerCardEventName.values().length;
    var playerCards = new ArrayList<PlayerCard>(estimatedSize);

    for (var city : City.Name.values()) {
      playerCards.add(PlayerCard.createCityCard(city));
    }

    for (var event : PlayerCardEventName.values()) {
      playerCards.add(PlayerCard.createEventCard(event));
    }

    Collections.shuffle(playerCards);

    return new MemoryCardDeck<>(playerCards);
  }

  public static MemoryCardDeck<InfectionCard> initInfectionDeck() {
    var infectionDeck = new ArrayList<InfectionCard>(City.Name.values().length);

    for (var city : City.Name.values()) {
      infectionDeck.add(new InfectionCard(city));
    }

    Collections.shuffle(infectionDeck);

    return new MemoryCardDeck<>(infectionDeck);
  }

  @Override
  public void addCard(T card) {
    this.deck.add(card);
  }

  @Override
  public T drawCard() {
    if (isEmpty()) {
      throw new NoMoreCardsInDeck("No more cards in deck");
    }

    return deck.removeLast();
  }

  @Override
  public T drawBottomCard() {
    if (isEmpty()) {
      throw new NoMoreCardsInDeck("No more cards in deck");
    }

    return deck.removeFirst();
  }

  @Override
  public int size() {
    return deck.size();
  }

  @Override
  public boolean isEmpty() {
    return deck.isEmpty();
  }

  @Override
  public List<Card> getCards() {
    return (List<Card>) deck;
  }

  public void shuffleEvenly(List<T> cards) {
    var cardsNumber = cards.size();
    var deckSize = deck.size();
    var parts = new ArrayList<List<T>>();
    int partSize = deckSize / cardsNumber;

    for (int i = 0; i < cardsNumber; i++) {
      var card = cards.get(i);
      int fromIndex = i * partSize;
      int toIndex = (i == cardsNumber - 1) ? deckSize : (i + 1) * partSize;

      var part = new ArrayList<>(deck.subList(fromIndex, toIndex));
      part.add(card);

      Collections.shuffle(part);

      parts.add(part);
    }

    deck.clear();

    for (var part : parts) {
      deck.addAll(part);
    }
  }
}
