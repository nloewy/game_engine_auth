package oogasalad.model.gameengine.player;

import java.util.List;
import java.util.Stack;
import oogasalad.model.api.PlayerRecord;
import oogasalad.model.gameengine.gameobject.Strikeable;
import oogasalad.model.gameengine.gameobject.scoreable.Scoreable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Player class represents a player in the game environment, managing their turns, scores,
 * and state.
 *
 * <p> Most methods in this class are protected, restricting their access to subclasses
 * within the player package. This encapsulation ensures that only authorized components
 * can modify the internal state of player objects, namely PlayerContainer.
 *
 * @author Noah Loewy
 */

public class Player {

  private static final Logger LOGGER = LogManager.getLogger(Player.class);
  private final int playerId;
  private List<Strikeable> myStrikeables;
  private List<Scoreable> myScoreables;
  private Strikeable activeStrikeable;
  private boolean roundCompleted;
  private int turnsCompleted;
  private double score;
  private Stack<PlayerRecord> playerHistory;

  /**
   * Initializes a player object given its unique id
   * @param id, the player's unique identifier
   */

  public Player(int id) {
    playerId = id;
    roundCompleted = false;
    turnsCompleted = 0;
    score = 0;
    playerHistory = new Stack<>();
  }

  /**
   * Adds the specified list of GameObjects that the player is able to strike.
   *
   * @param strikeables The list of strikeable objects to add.
   */

  public void addStrikeables(List<Strikeable> strikeables) {
    myStrikeables = strikeables;
    activeStrikeable = strikeables.get(strikeables.size()-1);
  }

  /**
   * Adds the specified list of GameObjects that the player earns points from.
   * @param scoreables The list of strikeable objects to add.
   */

  public void addScoreables(List<Scoreable> scoreables) {
    myScoreables = scoreables;
  }

  /**
   * Updates the active strikeable object based on the current state of strikeables by iterating
   * through the circular list of strikeables.
   */

  //TODO iterator maybe?
  public void updateActiveStrikeable() {
    if(myStrikeables.size()>1){
    activeStrikeable =
        myStrikeables.get((myStrikeables.indexOf(activeStrikeable) + 1) % myStrikeables.size());
    }
  }

  /**
   * Checks if the current round for the player is completed.
   *
   * @return True if the round is completed, otherwise false.
   */

  public boolean isRoundCompleted() {
    return roundCompleted;
  }

  /**
   * Marks the current round as completed for the player.
   */

  public void completeRound() {
    roundCompleted = true;
  }

  /**
   * Retrieves the ID of the active strikeable object.
   *
   * @return The ID of the active strikeable object.
   */

  public int getStrikeableID() {
    return activeStrikeable.asGameObject().getId();
  }

  /**
   * Marks the completion of a turn for the player.
   */

  public void completeTurn() {
    turnsCompleted++;
  }

  /**
   * Resets the state of the player's current round, turns, and delayed points at the beginning
   * of a new round .
   */

  public void startRound() {
    roundCompleted = false;
    turnsCompleted = 0;
    clearDelayedPoints();
  }

  /**
   * Retrieves the number of turns completed by the player in the current round.
   *
   * @return The number of turns completed in the current round.
   */

  protected int getTurnsCompleted() {
    return turnsCompleted;
  }


  /**
   * Applies delayed scores to the player's total score at the end of a turn.
   */

  protected void applyDelayedScore() {
    for (Scoreable o : myScoreables) {
      score += o.getTemporaryScore();
    }
  }

  /**
   * Retrieves the PlayerRecord representing the current state of the player.
   *
   * @return The PlayerRecord containing player information.
   */

  protected PlayerRecord getPlayerRecord() {
    try {
      double tempScore = score;
      for (Scoreable o : myScoreables) {
        tempScore += o.getTemporaryScore();
      }
      return new PlayerRecord(playerId, tempScore,
          activeStrikeable.asGameObject().getId());
    } catch (NullPointerException e) {
      LOGGER.warn("Player " + playerId + " not found");
      return null;
    }
  }

  protected void addPlayerHistory() {
    playerHistory.push(getPlayerRecord());
  }


  protected void toLastStaticStatePlayers() {
      score = playerHistory.peek().score();
  }

  //sets temporary score for player to 0
  private void clearDelayedPoints() {
    for (Scoreable o : myScoreables) {
      o.setTemporaryScore(0);
    }
  }





}
