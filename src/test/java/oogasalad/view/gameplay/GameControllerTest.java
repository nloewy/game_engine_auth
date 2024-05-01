package oogasalad.view.gameplay;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.view.controller.GameController;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.Start;
import util.DukeApplicationTest;

/**
 * Test class for GameController using DukeApplicationTest to simulate UI interactions.
 *
 * @author Doga Ozmen
 */
public class GameControllerTest extends DukeApplicationTest {

  private GameController gameController;
  private Scene scene;

  /**
   * Setup method to initialize the environment for each test.
   * @param stage The primary stage for this test.
   */
  @Start
  public void start(Stage stage) {
    gameController = new GameController(800, 600);
    scene = gameController.getScene();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Test pausing the game.
   */
  @Test
  public void testPauseGame() {
    runAsJFXAction(() -> gameController.pauseGame());
  }

  /**
   * Test resuming the game.
   */
  @Test
  public void testResumeGame() {
    runAsJFXAction(() -> gameController.resumeGame());

  }

  @Test
  public void testGameInitialization() {
    assertNotNull(gameController, "GameController should not be null after initialization.");
    assertNotNull(scene, "Scene should not be null after initialization.");
  }

  @Test
  public void testOpenAuthorEnvironment() {
    runAsJFXAction(() -> gameController.openAuthorEnvironment());
    // Verify that some authoring environment is displayed or initialized
    // This might need checking specific components if they are available through getters or observed via changes in the scene
  }

  @Test
  public void testGetGameTitles() {
    // Assuming gameController.getNewGameTitles() and gameController.getSavedGameTitles() should return non-empty lists
  //  assertFalse(gameController.getNewGameTitles().isEmpty(), "Should retrieve new game titles");
    assertFalse(gameController.getSavedGameTitles().isEmpty(), "Should retrieve saved game titles");
  }

  @Test
  public void testKeyInputHandling() {
    // Simulate a key press event and verify the controller handles it correctly
    runAsJFXAction(() -> {
      gameController.moveControllableX(true, 0, 100); // Assuming true means right movement
      // Verify that the game logic handled the movement correctly
      // This may require access to the game state or observing changes in the UI/scene
    });
  }



  /**
   * Test starting a game play.
   */
//  @Test
//  public void testStartGamePlay() {
//   // runAsJFXAction(() -> gameController.startGamePlay("data/playable_games/Sit chuffleboard
//   .json"));
//
//  }
}
