package com.example.pandemic.infrastructure.mapper;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class JooqPlayerMapper {

  //  public Player mapPlayerFromRecord(GamePlayersRecord record, Result<Record1<String>>
  // playerCards) {
  //    return Player.builder()
  //        .role(Player.Role.valueOf(record.getRole()))
  //        .numberOfAvailableActions(record.getNumberOfAvailableActions())
  //        .currentLocation(null)
  //        .cardsInHand(mapPlayerCardsFromRecord(playerCards))
  //        .build();
  //  }
  //
  //  private List<PlayerCard> mapPlayerCardsFromRecord(Result<Record1<String>> playerCards) {
  //    return playerCards.stream().map(s -> new PlayerCard(s.value1())).toList();
  //  }
}
