CREATE TABLE game
(
    id                             UUID PRIMARY KEY,
    state                          TEXT        NOT NULL,
    current_player_turn            INT         NOT NULL,
    infection_rate_marker_position INT         NOT NULL,
    outbreak_marker_position       INT         NOT NULL,
    black_disease_is_cured         BOOLEAN     NOT NULL DEFAULT FALSE,
    black_disease_is_eradicated    BOOLEAN     NOT NULL DEFAULT FALSE,
    red_disease_is_cured           BOOLEAN     NOT NULL DEFAULT FALSE,
    red_disease_is_eradicated      BOOLEAN     NOT NULL DEFAULT FALSE,
    yellow_disease_is_cured        BOOLEAN     NOT NULL DEFAULT FALSE,
    yellow_disease_is_eradicated   BOOLEAN     NOT NULL DEFAULT FALSE,
    blue_disease_is_cured          BOOLEAN     NOT NULL DEFAULT FALSE,
    blue_disease_is_eradicated     BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at                     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE players
(
    id                          UUID PRIMARY KEY,
    game_id                     UUID    NOT NULL REFERENCES game (id),
    role                        TEXT    NOT NULL,
    current_location            TEXT    NOT NULL,
    number_of_available_actions INT     NOT NULL DEFAULT 0,
    has_special_action_used     BOOLEAN NOT NULL DEFAULT FALSE,
    turn_order                  INT     NOT NULL
);

CREATE INDEX idx_players_game_id ON players (game_id);

CREATE TABLE player_cards
(
    id        UUID PRIMARY KEY,
    player_id UUID NOT NULL REFERENCES players (id),
    name      TEXT NOT NULL
);

CREATE INDEX idx_player_cards_player_id ON player_cards (player_id);

CREATE TABLE cities
(
    id                   UUID PRIMARY KEY,
    game_id              UUID    NOT NULL REFERENCES game (id),
    name                 TEXT    NOT NULL,
    has_research_station BOOLEAN NOT NULL default false,
    red_disease          INT     NOT NULL default 0,
    blue_disease         INT     NOT NULL default 0,
    yellow_disease       INT     NOT NULL default 0,
    black_disease        INT     NOT NULL default 0
);

CREATE INDEX idx_cities_game_id ON cities (game_id);

CREATE TABLE player_deck_cards
(
    id         UUID PRIMARY KEY,
    game_id    UUID NOT NULL REFERENCES game (id),
    name       TEXT NOT NULL,
    deck_order INT  NOT NULL
);

CREATE INDEX idx_player_deck_cards_game_id ON player_deck_cards (game_id);

CREATE TABLE player_discard_pile_cards
(
    id         UUID PRIMARY KEY,
    game_id    UUID NOT NULL REFERENCES game (id),
    name       TEXT NOT NULL,
    deck_order INT  NOT NULL
);

CREATE INDEX idx_player_discard_pile_cards_game_id ON player_discard_pile_cards (game_id);

CREATE TABLE infection_deck_cards
(
    id         UUID PRIMARY KEY,
    game_id    UUID NOT NULL REFERENCES game (id),
    name       TEXT NOT NULL,
    deck_order INT  NOT NULL
);

CREATE INDEX idx_infection_deck_cards_game_id ON infection_deck_cards (game_id);

CREATE TABLE infection_discard_pile_cards
(
    id         UUID PRIMARY KEY,
    game_id    UUID NOT NULL REFERENCES game (id),
    name       TEXT NOT NULL,
    deck_order INT  NOT NULL
);

CREATE INDEX idx_infection_discard_pile_cards_game_id ON infection_discard_pile_cards (game_id);