package com.example.pandemic.domain.action;

import com.example.pandemic.domain.Game;
import com.example.pandemic.domain.model.City;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class MedicMoveResolver {

  private MedicMoveResolver() {}

  // Rule: Medic automatically removes all cubes of a cured disease from their city
  public static void medicClearCuredDiseaseOnEnter(Player player, Game game) {
    if (!player.roleIs(Player.Role.MEDIC)) {
      return;
    }

    var destinationCity = game.cities().get(player.getCurrentLocation());

    Arrays.stream(Disease.Color.values())
        .filter(game::isDiseaseCured)
        .forEach(
            d -> {
              destinationCity.treatDisease(d, City.MAX_DISEASE_IN_CITY);

              log.info("Medic cure {} disease from {} where did he move", d, destinationCity);

              if (game.cities().isDiseaseEradicated(d)) {
                game.eradicateDisease(d);

                log.info("Disease {} is eradicated", d);
              }
            });
  }
}
