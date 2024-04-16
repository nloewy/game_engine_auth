package oogasalad.model.gameengine.turn;

import oogasalad.model.gameengine.player.PlayerContainer;

public class StandardTurnPolicy implements TurnPolicy {

  private final PlayerContainer playerContainer;

  public StandardTurnPolicy(PlayerContainer playerContainer) {
    this.playerContainer = playerContainer;
  }

  @Override
  public int getTurn() {
    int numPlayers = playerContainer.getNumPlayers();
    int turn = ((playerContainer.getActive()) % numPlayers) + 1;
    playerContainer.setActive(turn);
    System.out.println("preloop");
    while (playerContainer.getPlayer(turn).isRoundCompleted()) {
      System.out.println("loop");
      turn = (turn % numPlayers) + 1;
      playerContainer.setActive(turn);
    }

    playerContainer.getPlayer(turn).updateActiveControllableId();
    return turn;
  }
}
