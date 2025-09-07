# Pandemic board game



## Requirements:
- Java 24
- Docker

## How to run:
- docker-compose up -d
- ./gradlew bootRun
- check [postman collection](./docs/Pandemic.postman_collection.json) 

### Profiles
- prod - use postgres repository (jooq)
- dev - use memory repository

If want to run in dev mode, change in application.yml:

```yaml
spring:
  profiles:
    active: prod # dev | prod
```

## Game rules:
>  http://images-cdn.zmangames.com/us-east-1/filer_public/25/12/251252dd-1338-4f78-b90d-afe073c72363/zm7101_pandemic_rules.pdf

### Player turn:
- do 4 actions
- draw 2 cards
- infect cities

## todo
- CONTINGENCY PLANNER
- play event card action
- jooq
- api tests
- benchmark tests
- check number of cubes used - max 24 per disease
- max 6 research stations
