package com.example.pandemic.infrastructure.repository.Jooq;

import com.example.pandemic.domain.card.Card;
import com.example.pandemic.domain.card.CardDeck;
import com.example.pandemic.domain.exception.NoMoreCardsInDeck;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * Implementation of CardDeck using Jooq. To store which cards was removed and which was added,
 * to make less queries to database after save game.
 */
class JooqCardDeck<T extends Card> implements CardDeck<T> {

  private final List<T> cards;
  @Getter private final List<T> removedCards = new ArrayList<>();
  @Getter private final List<T> addedCards = new ArrayList<>();

  public JooqCardDeck(List<T> cards) {
    this.cards = cards;
  }

  @Override
  public void addCard(T card) {
    cards.add(card);

    addedCards.add(card);
  }

  @Override
  public T drawCard() {
    if (isEmpty()) {
      throw new NoMoreCardsInDeck("No more cards in deck");
    }

    var card = cards.removeLast();

    removedCards.add(card);

    return card;
  }

  @Override
  public T drawBottomCard() {
    if (isEmpty()) {
      throw new NoMoreCardsInDeck("No more cards in deck");
    }

    var card = cards.removeFirst();

    removedCards.add(card);

    return card;
  }

  @Override
  public int size() {
    return cards.size();
  }

  @Override
  public boolean isEmpty() {
    return cards.isEmpty();
  }

  @Override
  public List<Card> getCards() {
    return (List<Card>) cards;
  }
}
