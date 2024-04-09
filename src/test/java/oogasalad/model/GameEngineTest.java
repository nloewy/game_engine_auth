package oogasalad.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.Pair;
import oogasalad.model.api.CollidableRecord;
import oogasalad.model.api.GameRecord;
import oogasalad.model.gameengine.GameEngine;
import oogasalad.model.gameengine.collidable.Collidable;
import oogasalad.model.gameengine.collidable.CollidableContainer;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

public class GameEngineTest {


  private GameEngine gameEngine;
  private CollidableRecord c1;
  private CollidableRecord c2;
  private CollidableRecord c3;
  private CollidableContainer container;

  private static final double DELTA = .0001;


  @BeforeEach
  public void setUp() {
    gameEngine = new GameEngine("testPhysics");
    container = gameEngine.getCollidableContainer();
  }

  private boolean isStatic(GameRecord r) {
    for(CollidableRecord cr : r.collidables()) {
      if(cr.velocityY()!=0 || cr.velocityX()!=0) {
        return false;
      }
    }
    return true;
  }
  @Test
  public void testStartAndResetGame() {
    // Ensure the game starts without errors

    // Assert that the initial round and turn are as expected
    assertEquals(1, gameEngine.getRound());
    assertEquals(1, gameEngine.getTurn());
    // Reset the game and verify that it's back to initial state
    gameEngine.reset();
    assertEquals(1, gameEngine.getRound());
    assertEquals(1, gameEngine.getTurn());
  }


  @Test
  public void testOnApplyVelocity() {
    // Ensure the game starts without errors

    gameEngine.applyInitialVelocity(10, 0, 1);
    // Assert that the initial round and turn are as expected
    assertEquals(10, container.getCollidableRecord(1).velocityX());
  }

  @Test
  public void testSingularUpdate() {
  // Ensure the game starts without errors

  gameEngine.applyInitialVelocity(10, 0, 1);
  // Assert that the initial round and turn are as expected
  assertEquals(10, container.getCollidableRecord(1).velocityX());
  System.out.println(container.getCollidableRecord(1));
  gameEngine.update(1.0/4.0);
  System.out.println(container.getCollidableRecord(1));
  assertEquals(2.5, container.getCollidableRecord(1).x());
  assertEquals(5, container.getCollidableRecord(1).velocityX());
  }


   @Test
  public void testMultipleUpdate() {
    // Ensure the game starts without errors
    gameEngine.applyInitialVelocity(15, 0, 1);
    assertEquals(15, container.getCollidableRecord(1).velocityX());
    gameEngine.update(1.0/4);
    assertEquals(15/4.0, container.getCollidableRecord(1).x());
    assertEquals(10, container.getCollidableRecord(1).velocityX());
    gameEngine.update(1.0/4);
    assertEquals(25/4.0, container.getCollidableRecord(1).x());
    assertEquals(5, container.getCollidableRecord(1).velocityX());
  }


  @Test
  public void testStop() {
    // Ensure the game starts without errors
    gameEngine.applyInitialVelocity(15, Math.PI/2, 1);
    System.out.println(gameEngine.getCollidableContainer().getCollidableRecord(1));
    GameRecord r = gameEngine.update(1.0/4);
    System.out.println(r.collidables().get(0));
    while(!isStatic(r)) {
      r = gameEngine.update(1.0/4);
      System.out.println(r.collidables().get(0));

    }
    assertEquals(7.5, container.getCollidableRecord(1).y());
    assertEquals(0, container.getCollidableRecord(1).velocityY());
  }


  @Test
  public void testMoveAtAngle() {
    gameEngine.applyInitialVelocity(20, Math.PI/4, 1);
    gameEngine.update(.5);
    System.out.println(container.getCollidableRecord(1));
    assertEquals(10/Math.sqrt(2), container.getCollidableRecord(1).x(), DELTA);
    assertEquals(10/Math.sqrt(2), container.getCollidableRecord(1).y(), DELTA);
    assertEquals(10/Math.sqrt(2), container.getCollidableRecord(1).velocityX(), DELTA);
    assertEquals(10/Math.sqrt(2), container.getCollidableRecord(1).velocityX(), DELTA);
  }


  /**
   *
  @Test
  public void testAdvanceTurnAndAdjustPoints() {
    // Ensure the game starts without errors
    gameEngine.handleCollisions(List.of(new Pair(1, 4)), 1.0/40);
    assertEquals(2, gameEngine.getTurn());
    assertEquals(10, gameEngine.getPlayerContainer().getPlayer(1).getVariable("score"));
  }

  @Test
  public void testMoveWithoutCollision() {
    // Ensure the game starts without errors
    gameEngine.applyInitialVelocity(10.0,0,1);
    gameEngine.update(1);
    gameEngine.handleCollisions(List.of(), 1);
    assertEquals(10,
        gameEngine.getCollidableContainer().getCollidable(1).getCollidableRecord().velocityX());
    assertEquals(10,
        gameEngine.getCollidableContainer().getCollidable(1).getCollidableRecord().x());
  }


  @Test
  public void testUndoCommand() {
    // Ensure the game starts without errors
    gameEngine.applyInitialVelocity(2,0,8);
    gameEngine.update(1.0/40);
    gameEngine.handleCollisions(List.of(new Pair(8,2)), 1.0/40); //move
    gameEngine.update(1.0/40);
    gameEngine.applyInitialVelocity(2,Math.PI/2,8);
    gameEngine.update(1.0/40);
    gameEngine.handleCollisions(List.of(new Pair(8,2)), 1.0/40); //move
    gameEngine.update(1.0/40);
    gameEngine.handleCollisions(List.of(new Pair(7,8),new Pair(8,2)), 1.0/40);
    assertEquals(0,
        gameEngine.getCollidableContainer().getCollidable(8).getCollidableRecord().velocityX());

    assertEquals(0,
        gameEngine.getCollidableContainer().getCollidable(8).getCollidableRecord().velocityY());
    assertEquals(48.0,
        gameEngine.getCollidableContainer().getCollidable(8).getCollidableRecord().y());
    assertEquals(50,
        gameEngine.getCollidableContainer().getCollidable(8).getCollidableRecord().x());

    gameEngine.handleCollisions(List.of(new Pair(7,8),new Pair(8,2)), 1.0/40);

    assertEquals(0,
        gameEngine.getCollidableContainer().getCollidable(8).getCollidableRecord().velocityX());

    assertEquals(0,
        gameEngine.getCollidableContainer().getCollidable(8).getCollidableRecord().velocityY());
    assertEquals(48.0,
        gameEngine.getCollidableContainer().getCollidable(8).getCollidableRecord().y());
    assertEquals(48.0,
        gameEngine.getCollidableContainer().getCollidable(8).getCollidableRecord().x());

  }
*/
}
