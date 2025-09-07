package com.example.pandemic.domain.card;

public enum PlayerCardEventName {
  // draw, look at, and rearrange the top 6 cards of infection deck. Put them back on top.
  FORECAST,
  // move any 1 player to any city
  AIRLIFT,
  // skip next infect cities step (do not flip any infection cards)
  ONE_QUIET_NIGHT,
  // add 1 research station to any city (no city card needed)
  GOVERNMENT_GRANT,
  // remove any 1 card in the infection discard pile from the game.
  // You may play this between the infect and intensify steps of and epidemic.
  RESILIENT_POPULATION
}
