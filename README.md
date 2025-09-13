# Pandemic board game

Pandemic is a digital implementation of the popular board game “Pandemic.”
It simulates a cooperative game where players work together to discover cures
for diseases spreading across the world while managing outbreaks, infections.

This Java-based application models the core mechanics of Pandemic: player actions,
infection spread, outbreaks, research stations, and various win/lose conditions.

The code supports multiple players, role-based gameplay, and the standard phases:
setup, player turns (actions, draws), infection, and ending conditions.

### Game rules:

> http://images-cdn.zmangames.com/us-east-1/filer_public/25/12/251252dd-1338-4f78-b90d-afe073c72363/zm7101_pandemic_rules.pdf

## Requirements:

- Java 24
- Docker

## How to run:

- docker-compose up -d
- currently need to run manually src/main/resources/sql/init-schema.sql on DB
- ./gradlew bootRun
- check [postman collection](./docs/Pandemic.postman_collection.json)

## Concept

### Game flow

#### Setup

- Select roles for players.
- Distribute starting cards.
- Place initial infection cubes (9 cities infected).

#### Player Turn

- Actions phase: Player performs up to 4 actions:
    - Drive/Ferry (move to adjacent city).
    - Direct Flight (discard card, move to city on card).
    - Charter Flight (discard card of current city, move anywhere).
    - Shuttle Flight (move between research stations).
    - Build Research Station (discard city card of current location).
    - Discover Cure (5 cards of one color at a research station).
    - Treat Disease (remove cubes).
    - Share Knowledge (give/take card of current city).
- Draw phase: Draw 2 player cards. If Epidemic → increase infection rate, infect bottom card city,
  reshuffle discard pile onto top.
- Infect phase: Draw infection cards equal to infection rate.

#### End Conditions

- Win: All diseases cured.
- Lose if:
    - Outbreak counter reaches 8.
    - Not enough cubes of a disease to place.
    - Player deck is exhausted.

### Main model: **Game**

- Represents a single Pandemic session.
- Holds global state, including:
    - Phase / State – e.g. SETUP, ACTION, DRAW, INFECT, GAME_OVER.
    - Players – list of players participating.
    - Board – all cities, diseases, stations, etc.
    - Decks – Player Deck and Infection Deck.
- Enforces turn order and legal actions.

## Profiles

- prod - use postgres repository (jooq)
- dev - use memory repository

If want to run in dev mode:

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## Todo

- CONTINGENCY PLANNER
- play event card action
- deploy on ec2
- benchmark tests
- migrations tool
- check number of cubes used - max 24 per disease
- max 6 research stations
