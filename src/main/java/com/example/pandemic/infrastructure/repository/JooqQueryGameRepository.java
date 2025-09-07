package com.example.pandemic.infrastructure.repository;

import static com.example.jooq.generated.Tables.*;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

import com.example.pandemic.application.query.CityDto;
import com.example.pandemic.application.query.GameDto;
import com.example.pandemic.application.query.PlayerDto;
import com.example.pandemic.application.query.QueryGameRepository;
import com.example.pandemic.domain.Game;
import com.example.pandemic.infrastructure.mapper.JooqQueryGameMapper;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Result;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("prod")
@RequiredArgsConstructor
public class JooqQueryGameRepository implements QueryGameRepository {

  private final DSLContext dsl;

  private final JooqQueryGameMapper mapper;

  @Override
  public GameDto get(Game.@NonNull Id gameId) {
    return dsl.selectFrom(GAME).where(GAME.ID.eq(gameId.value())).fetchOne(mapper::mapGame);
  }

  @Override
  public List<PlayerDto> getPlayers(Game.@NonNull Id gameId) {
    return dsl.select(PLAYERS, playerCards())
        .from(PLAYERS)
        .where(PLAYERS.GAME_ID.eq(gameId.value()))
        .fetch(mapper::mapPlayer);
  }

  private Field<Result<Record1<String>>> playerCards() {
    return multiset(
        select(PLAYER_CARDS.NAME).from(PLAYER_CARDS).where(PLAYER_CARDS.PLAYER_ID.eq(PLAYERS.ID)));
  }

  @Override
  public List<CityDto> getCities(Game.@NonNull Id gameId) {
    return dsl.selectFrom(CITIES).where(CITIES.GAME_ID.eq(gameId.value())).fetch(mapper::mapCity);
  }
}
