package oogasalad.view.scene_management;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javax.xml.parsers.ParserConfigurationException;
import oogasalad.model.api.GameRecord;
import oogasalad.view.controller.GameController;
import oogasalad.view.enums.ThemeType;
import oogasalad.view.visual_elements.CompositeElement;
import org.xml.sax.SAXException;

/**
 * Manages different screens (scenes) within the game, such as the title screen, menu screen, game
 * screen, and transition screen. It updates and transitions between screens based on game state and
 * player interactions.
 *
 * @author Doga Ozmen, Jordan Haytaian
 */
public class SceneManager {

  private final Pane root;
  private final Scene scene;
  private final SceneElementParser sceneElementParser;
  private final SceneElementFactory sceneElementFactory;
  private final SceneElementStyler sceneElementStyler;
  private CompositeElement compositeElement;
  private GameStatusManager gameStatusManager;
  private Pane pauseElements;
  private Pane transitionElements;
  private Pane strikingElements;
  private int currentRound;
  private final String titleSceneElementsPath = "data/scene_elements/titleSceneElements.xml";
  private final String menuSceneElementsPath = "data/scene_elements/menuSceneElements.xml";
  private final String gameManagementElementsPath = "data/scene_elements/gameManagementElements.xml";
  private final String transitionElementsPath = "data/scene_elements/transitionElements.xml";
  private final String gameOverSceneElementsPath = "data/scene_elements/gameOverElements.xml";
  private final String pausePath = "data/scene_elements/pauseElements.xml";
  private final String strikingElementsPath = "data/scene_elements/strikingElements.xml";


  /**
   * Constructor initializes scene, root, sceneElementParser, and sceneElementFactory which are
   * necessary to update scenes with new elements
   *
   * @param gameController handles model/view interactions
   * @param screenWidth    screen width to be used for scaling ratio based elements
   * @param screenHeight   screen height to be used for scaling ratio based elements
   */
  public SceneManager(GameController gameController, double screenWidth,
      double screenHeight) {
    root = new Pane();
    scene = new Scene(root);

    sceneElementParser = new SceneElementParser();
    sceneElementStyler = new SceneElementStyler(root);
    gameStatusManager = new GameStatusManager();
    sceneElementFactory = new SceneElementFactory(screenWidth, screenHeight, sceneElementStyler,
        new SceneElementHandler(gameController, this, gameStatusManager));
  }

  /**
   * Getter for the scene
   *
   * @return scene displaying game visuals
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * Creates a title scene by resetting the root and creating new elements from title scene xml
   * file
   */
  public void createTitleScene() {
    resetRoot();
    root.getChildren().add(createSceneElements(titleSceneElementsPath));
  }

  /**
   * Creates a menu scene by resetting the root and creating new elements from menu scene xml file
   */
  public void createMenuScene() {
    resetRoot();
    root.getChildren().add(createSceneElements(menuSceneElementsPath));
  }

  /**
   * Called when game is resumed, removes pause screen elements
   */
  public void removePauseSheen() {
    root.getChildren().remove(pauseElements);
  }

  /**
   * Updates game elements and stat display from GameRecord info
   *
   * @param gameRecord represents updated state of game
   */
  public void update(GameRecord gameRecord) {
    compositeElement.update(gameRecord.gameObjectRecords());
    gameStatusManager.update(gameRecord.players(), gameRecord.turn(), gameRecord.round());
    root.requestFocus();
    checkEndRound(gameRecord);
  }

  public void displayStrikingElements() {
    if (!root.getChildren().contains(strikingElements)) {
      root.getChildren().add(strikingElements);
    }
  }

  void hideStrikingElements() {
    root.getChildren().remove(strikingElements);
  }

  /**
   * Called when next round is started, removes transition screen elements
   */
  void removeTransitionSheen() {
    root.getChildren().remove(transitionElements);
    root.requestFocus();
  }

  /**
   * Getter for root node
   *
   * @return root node of scene
   */
  Pane getRoot() {
    return root;
  }

  /**
   * Creates a pause display by adding elements created from pause xml file
   */
  void createPauseDisplay() {
    if (!root.getChildren().contains(pauseElements)) {
      root.getChildren().add(pauseElements);
    }
  }

  /**
   * Changes the theme by prompting the element styler to switch style sheets
   * @param selectedTheme theme selected by user
   */
  void changeTheme(ThemeType selectedTheme){
    sceneElementStyler.changeTheme(selectedTheme);
  }

  /**
   * Makes the game screen including game board elements, striker input, and game stat display
   *
   * @param compositeElement game board elements
   * @param gameRecord       holds score, turn, and round info for stat display
   */
  public void makeGameScreen(CompositeElement compositeElement, GameRecord gameRecord) {
    this.compositeElement = compositeElement;
    pauseElements = createSceneElements(pausePath);
    transitionElements = createSceneElements(transitionElementsPath);
    strikingElements = createSceneElements(strikingElementsPath);
    addGameManagementElementsToGame(gameRecord);
    addGameElementsToGame();
    root.requestFocus();
  }


  private void createTransitionDisplay() {
    root.getChildren().add(transitionElements);
  }

  private void createGameOverScene() {
    resetRoot();
    root.getChildren().add(createSceneElements(gameOverSceneElementsPath));
  }


  private Pane createSceneElements(String filePath) {
    try {
      List<Map<String, String>> sceneElementParameters = sceneElementParser.getElementParametersFromFile(
          filePath);
      return sceneElementFactory.createSceneElements(sceneElementParameters);

    } catch (ParserConfigurationException | SAXException | IOException e) {
      //TODO: Exception Handling
      return null;
    }
  }

  private void addGameManagementElementsToGame(GameRecord gameRecord) {
    resetRoot();
    currentRound = gameRecord.round();
    Pane sceneElements = createSceneElements(gameManagementElementsPath);
    root.getChildren().addAll(sceneElements);

  }

  private void addGameElementsToGame() {
    compositeElement.addElementsToRoot(root);
  }

  private void resetRoot() {
    root.getChildren().clear();
  }

  private void checkEndRound(GameRecord gameRecord) {
    if (gameRecord.gameOver()) {
      createGameOverScene();
      gameStatusManager.update(gameRecord.players(), gameRecord.turn(), gameRecord.round());
    } else if (gameRecord.round() != currentRound) {
      currentRound = gameRecord.round();
      createTransitionDisplay();

    }
  }
}
