package com.example.pandemic.domain;

import com.example.common.BaseId;
import com.example.common.Result;
import com.example.pandemic.domain.action.request.ActionRequest;
import com.example.pandemic.domain.card.CardDeck;
import com.example.pandemic.domain.card.InfectionCard;
import com.example.pandemic.domain.card.MemoryCardDeck;
import com.example.pandemic.domain.card.PlayerCard;
import com.example.pandemic.domain.collection.Cities;
import com.example.pandemic.domain.collection.CitiesCollection;
import com.example.pandemic.domain.exception.InvalidNumberOfPlayers;
import com.example.pandemic.domain.exception.InvalidPlayer;
import com.example.pandemic.domain.model.Disease;
import com.example.pandemic.domain.model.Player;
import com.example.pandemic.domain.step.GameStepProvider;
import com.example.pandemic.domain.track.DiseaseTrack;
import com.example.pandemic.domain.track.InfectionRateTrack;
import com.example.pandemic.domain.track.OutbreakTrack;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;

@AllArgsConstructor
public final class Game {

  private static final int PLAYER_HAND_LIMIT = 7;
  private static final int MIN_PLAYER_COUNT = 2;
  private static final int MAX_PLAYER_COUNT = 4;

  @NonNull @Getter private final Id id;
  @Getter @NonNull private final InfectionRateTrack infectionRateTrack;
  @Getter @NonNull private final OutbreakTrack outbreakTrack;
  @NonNull @Getter private final List<Player> players;
  @NonNull @Delegate private final DiseaseTrack diseaseTrack;

  @NonNull
  @Getter
  @Accessors(fluent = true)
  private final CitiesCollection cities;

  @NonNull @Getter private final CardDeck<PlayerCard> playerDeck;
  @NonNull @Getter private final CardDeck<PlayerCard> playerDiscardPile;
  @NonNull @Getter private final CardDeck<InfectionCard> infectionDeck;
  @NonNull @Getter private final CardDeck<InfectionCard> infectionDiscardPile;
  @NonNull private Game.State state;
  @Getter private int currentPlayerTurnIndex;

  public static Game create(
      List<Player> players,
      Cities cities,
      CardDeck<PlayerCard> playerDeck,
      CardDeck<InfectionCard> infectionDeck,
      CardDeck<InfectionCard> infectionDiscardPile) {
    if (players.size() < MIN_PLAYER_COUNT || players.size() > MAX_PLAYER_COUNT) {
      throw new InvalidNumberOfPlayers("Invalid number of players - min 2 and max 4 players");
    }

    players.getFirst().resetAvailableActions();

    return new Game(
        Id.generate(),
        InfectionRateTrack.init(),
        OutbreakTrack.init(),
        players,
        DiseaseTrack.init(),
        cities,
        playerDeck,
        MemoryCardDeck.init(),
        infectionDeck,
        infectionDiscardPile,
        State.ACTION,
        0);
  }

  public Result<String, String> performAction(@NonNull ActionRequest actionRequest) {
    var gameStep = GameStepProvider.determineGameStep(state);

    if (checkAnyPlayerShouldDiscardCardsToSevenOnHand()) {
      return new Result.Failure<>("Too many cards in player hand");
    }

    var result = gameStep.executeAction(this, actionRequest);

    if (!result.isSuccess()) {
      return result;
    }

    if (gameStep.canMoveToNextStep(this)) {
      gameStep.moveToNextStep(this);
    }

    return result;
  }

  public void discoverCure(Disease.Color disease) {
    diseaseTrack.cureDisease(disease);

    if (diseaseTrack.areAllDiseasesCured()) {
      win();
    }
  }

  public Player getActivePlayer() {
    return players.get(currentPlayerTurnIndex);
  }

  public void goToSecondDrawStepOrInfectStep() {
    if (state == State.FIRST_DRAW) {
      state = State.SECOND_DRAW;

      return;
    }

    if (!state.equals(State.SECOND_DRAW)) {
      throw new IllegalStateException("Game is not in second draw state to move to infect state");
    }

    state = State.INFECT;
  }

  public void goToDrawState() {
    if (!state.equals(State.ACTION)) {
      throw new IllegalStateException("Game is not in action state to move to first draw state");
    }

    state = State.FIRST_DRAW;
  }

  public void goToActionState() {
    if (!state.equals(State.INFECT)) {
      throw new IllegalStateException("Game is not in infect state to move to action state");
    }

    state = State.ACTION;

    goToNextPlayer();
  }

  private void goToNextPlayer() {
    currentPlayerTurnIndex++;

    if (currentPlayerTurnIndex == players.size()) {
      currentPlayerTurnIndex = 0;
    }

    getActivePlayer().resetAvailableActions();
  }

  public int getInfectionRate() {
    return infectionRateTrack.getCurrentValue();
  }

  public void moveOutbreakTrack() {
    outbreakTrack.moveMarker();

    if (outbreakTrack.isOnLastPosition()) {
      lose();
    }
  }

  public String getState() {
    return state.name();
  }

  public void moveInfectionRateMarker() {
    infectionRateTrack.moveMarker();
  }

  public Player getPlayer(int playerIndex) {
    if (playerIndex < 0 || playerIndex >= players.size()) {
      throw new InvalidPlayer("Invalid player index");
    }

    return players.get(playerIndex);
  }

  private void win() {
    state = State.WIN;
  }

  public void lose() {
    state = State.LOSE;
  }

  public boolean isFirstDraw() {
    return state.equals(State.FIRST_DRAW);
  }

  private boolean checkAnyPlayerShouldDiscardCardsToSevenOnHand() {
    if (!EnumSet.of(State.ACTION, State.FIRST_DRAW, State.INFECT).contains(state)) {
      return false;
    }

    return players.stream().anyMatch(player -> player.getNumberOfCardsInHand() > PLAYER_HAND_LIMIT);
  }

  public Optional<Player> findPlayer(Player.Role role) {
    return players.stream().filter(p -> p.roleIs(role)).findFirst();
  }

  public enum State {
    ACTION,
    FIRST_DRAW,
    SECOND_DRAW,
    INFECT,
    WIN,
    LOSE
  }

  public record Id(@NonNull UUID value) implements BaseId {
    public static Id generate() {
      return new Id(UUID.randomUUID());
    }

    public static Id from(@NonNull UUID id) {
      return new Id(id);
    }
  }
}
