package oogasalad.model.gameengine.turn;

import java.util.Collection;
import java.util.List;
import oogasalad.model.annotations.CommandHelpInfo;
import oogasalad.model.annotations.IsCommand;
import oogasalad.model.gameengine.player.Player;
import oogasalad.model.gameengine.player.PlayerContainer;

/**
 * The StandardTurnPolicy class implements the TurnPolicy interface by defining a standard turn
 * policy where players take turns in a sequential order. This turn policy ensures that each player
 * takes a turn in order, skipping players who have completed their rounds until an active player is
 * found. Essentially, a circular list is used.
 *
 * @author Noah Loewy
 */

@IsCommand(isCommand = true)
@CommandHelpInfo(description = "")
public class StandardTurnPolicy implements TurnPolicy {

  private final PlayerContainer playerContainer;

  /**
   * Initializes a StandardTurnPolicy object with the specified player container.
   *
   * @param playerContainer The player container containing the players in the game.
   */

  public StandardTurnPolicy(PlayerContainer playerContainer) {
    this.playerContainer = playerContainer;
  }

  /**
   * Retrieves the player's turn based on the standard turn policy, which utilizes a circular list.
   *
   * @return The id of the player whose turn is next.
   */

  @Override
  public int getNextTurn() {
    int numPlayers = playerContainer.getPlayers().size();
    Collection<Player> players = playerContainer.getPlayers();
    int turn = getTurn(numPlayers, players);
    while (playerContainer.getActive().isRoundCompleted()) {
      turn = getTurn(numPlayers, players);
    }
    playerContainer.getActive().updateActiveStrikeable();
    return turn;
  }

  //gets the actual next turn
  private int getTurn(int numPlayers, Collection<Player> players) {
    int turn = ((playerContainer.getActive().getId()) % numPlayers) + 1;
    players.stream()
        .filter(p -> p.getId() == turn)
        .findFirst()
        .ifPresent(playerContainer::setActive);
    return turn;
  }
}
