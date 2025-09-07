package com.example.pandemic.domain.card;

import java.util.List;

public interface CardDeck<T extends Card> {

  void addCard(T card);

  T drawCard();

  T drawBottomCard();

  int size();

  boolean isEmpty();

  List<Card> getCards();
}
