package com.example.pandemic.domain.service;

import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.card.InfectionCard;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class InfectCityFactory {

  public static Infector createInfector() {
    return new Infector();
  }

  public static class Infector {

    private final List<City.Name> citiesInfectedWithOutbreak = new ArrayList<>();

    public void infect(Game game, InfectionCard infectionCard) {
      infect(game, infectionCard.getCityName(), infectionCard.getDiseaseColor());
    }

    public void infect(Game game, City.Name cityName, Disease.Color disease) {
      if (shouldNotInfect(game, cityName, disease)) {
        return;
      }

      var city = game.cities().get(cityName);
      var isOutbreak = city.addDisease(disease, 1);

      log.info("Infected city: {}", cityName);

      if (isOutbreak) {
        makeOutbreak(game, cityName, disease);
      }
    }

    private void makeOutbreak(Game game, City.Name cityName, Disease.Color disease) {
      log.info("Outbreak in city: {}", cityName);

      citiesInfectedWithOutbreak.add(cityName);

      game.moveOutbreakTrack();

      for (var connectedCity : CityConnections.getConnections(cityName)) {
        infect(game, connectedCity, disease);
      }
    }

    private boolean shouldNotInfect(Game game, City.Name cityName, Disease.Color disease) {
      if (game.isDiseaseEradicated(disease)) {
        log.info("Disease {} is eradicated - no infect in city {} ", disease, cityName);

        return true;
      }

      if (citiesInfectedWithOutbreak.contains(cityName)) {
        log.info("City {} was infected with outbreak - no infect", cityName);

        return true;
      }

      if (game.isDiseaseCured(disease) && isMedicLocation(game, cityName)) {
        log.info("Disease {} is cured and is medic location {} - no infect", disease, cityName);

        return true;
      }

      if (isCityIsOrIsConnectedToQuarantineSpecialistLocation(game, cityName)) {
        log.info(
            "City {} is or is connected to quarantine specialist location - no infect", cityName);

        return true;
      }

      return false;
    }

    private boolean isMedicLocation(Game game, City.Name cityName) {
      return game.findPlayer(Player.Role.MEDIC)
          .filter(p -> p.getCurrentLocation().equals(cityName))
          .isPresent();
    }

    private boolean isCityIsOrIsConnectedToQuarantineSpecialistLocation(
        Game game, City.Name cityName) {
      return game.findPlayer(Player.Role.QUARANTINE_SPECIALIST)
          .filter(
              p ->
                  p.getCurrentLocation().equals(cityName)
                      || CityConnections.hasConnection(p.getCurrentLocation(), cityName))
          .isPresent();
    }
  }
}
