package oogasalad.view.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import oogasalad.model.api.ExternalGameEngine;
import oogasalad.model.api.GameRecord;
import oogasalad.model.api.ViewGameObjectRecord;
import oogasalad.model.api.data.GameData;
import oogasalad.model.api.data.GameObjectProperties;
import oogasalad.model.api.data.GlobalVariables;
import oogasalad.model.api.data.ParserPlayer;
import oogasalad.model.api.data.Position;
import oogasalad.model.api.data.Variables;
import oogasalad.model.api.exception.InvalidColorParsingException;
import oogasalad.model.api.exception.InvalidFileException;
import oogasalad.model.api.exception.InvalidImageException;
import oogasalad.model.api.exception.InvalidShapeException;
import oogasalad.model.api.exception.MissingJsonGameInfoException;
import oogasalad.model.gameengine.GameEngine;
import oogasalad.model.gameparser.GameLoaderView;
import oogasalad.view.Warning;
import oogasalad.view.api.enums.AuthoringImplementationType;
import oogasalad.view.api.enums.KeyInputType;
import oogasalad.view.api.enums.SupportedLanguage;
import oogasalad.view.api.enums.UITheme;
import oogasalad.view.database.Leaderboard;
import oogasalad.view.scene_management.GameWindow;
import oogasalad.view.scene_management.element_parsers.GameTitleParser;
import oogasalad.view.scene_management.scene_managers.AnimationManager;
import oogasalad.view.scene_management.scene_managers.SceneManager;
import oogasalad.view.visual_elements.CompositeElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GameController class handles communications between model and view.  This class holds manager
 * class instances to delegate handling the information received from the model.
 *
 * @author Jordan Haytaian, Judy He, Doga Ozmen
 */
public class GameController {

  private static final String RESUME_GAME_DATA_FOLDER = "data/resume_game/";
  private static final String INCREASING = "LowestScoreComparator";
  private static final Logger LOGGER = LogManager.getLogger(GameController.class);
  private static final Warning WARNING = new Warning();
  private final SceneManager sceneManager;
  private final AnimationManager animationManager;
  private final GameTitleParser gameTitleParser;
  private final int maxVelocity;
  private ExternalGameEngine gameEngine;
  private GameLoaderView gameLoaderView;
  private final DatabaseController databaseController;
  private Map<Integer, String> playerMap;
  private boolean ableToStrike;
  private String selectedGame;

  /**
   * Initializes the GameController with the specified screen width and height.
   *
   * <p>
   * The constructor creates an instance of `SceneManager` with the provided dimensions and the
   * current GameController as the owner, and then initializes the title scene. Additionally, an
   * `AnimationManager` instance is created to handle game animations, and the controller is set to
   * be able to strike by default. The max velocity is initialized.
   * </p>
   *
   * @param width  The width of the screen for the game.
   * @param height The height of the screen for the game.
   */
  public GameController(double width, double height) {

    List<String> currentPlayersManager = new ArrayList<>();
    databaseController = new DatabaseController(new Leaderboard(), currentPlayersManager);
    sceneManager = new SceneManager(this, databaseController, width, height, currentPlayersManager);
    animationManager = new AnimationManager();
    gameTitleParser = new GameTitleParser();
    ableToStrike = true;
    maxVelocity = 1000;
  }

  /**
   * Getter for scene to display on stage
   */
  public Scene getScene() {
    return sceneManager.getScene();
  }

  /**
   * Pauses the game by switching to a pause scene and pausing all animations.
   *
   * <p>
   * This method triggers the transition to a pause scene, allowing the player to take a break or
   * adjust settings. It also pauses all ongoing animations to halt the game's progression during
   * the pause.
   * </p>
   */
  public void pauseGame() {
    animationManager.pauseAnimation();
  }

  /**
   * Resumes the game by removing pause sheen elements and resuming all animations.
   *
   * <p>
   * This method removes the pause sheen and transitions back to the previous scene, allowing the
   * game to continue as it was before the pause. It also resumes any animations that were paused.
   * </p>
   */
  public void resumeGame() {
    sceneManager.removePauseSheen();
    animationManager.resumeAnimation();
  }

  /**
   * Opens the authoring environment for creating game content.
   *
   * <p>
   * This method creates a new instance of the `NewAuthoringController` and uses it to update the
   * authoring screen. This allows the user to access the authoring environment, where they can
   * create game content as needed.
   * </p>
   */
  public void openAuthorEnvironment() {
    AuthoringController newAuthoringController = new AuthoringController(SupportedLanguage.ENGLISH,
        UITheme.DEFAULT, AuthoringImplementationType.DEFAULT,
        databaseController.getPlayerNames().get(0));
    newAuthoringController.updateAuthoringScreen();
  }

  /**
   * Starts the selected game by loading necessary back end components, creating the scene, and
   * starting the animation
   *
   * @param selectedGame the game title selected to play
   */
  public void startGamePlay(String selectedGame) {
    try {
      gameLoaderView = new GameLoaderView(selectedGame);
      gameEngine = new GameEngine(selectedGame);
    } catch (InvalidFileException e) {
      
      LOGGER.error(e.getMessage());
      handleException("Start Game Error", "Invalid game file");
      return;
    } catch (InvalidColorParsingException | InvalidShapeException |
             MissingJsonGameInfoException e) {
      
      LOGGER.error(e.getMessage());
      handleException("Parsing Error", e.getMessage());
      return;
    }

    List<String> players = databaseController.getPlayerNames();
    playerMap = IntStream.range(1, players.size() + 1)
        .boxed()
        .collect(Collectors.toMap(
            i -> i,
            i -> players.get(i - 1)
        ));
    GameRecord gameRecord = gameEngine.restoreLastStaticGameRecord();
    CompositeElement compositeElement = createCompositeElementFromGameLoader();
    sceneManager.makeGameScene(compositeElement, gameRecord);
    this.selectedGame = selectedGame.substring(selectedGame.lastIndexOf("/") + 1);
    sceneManager.update(gameRecord, playerMap, selectedGame);
  }

  /**
   * Sends velocity and angle to back end to simulate hitting point scoring object
   *
   * @param fractionalVelocity velocity as fraction of maxVelocity
   */
  public void hitPointScoringObject(double fractionalVelocity, double angle) {
    if (ableToStrike) {
      gameEngine.applyInitialVelocity(maxVelocity * fractionalVelocity, angle);
      ableToStrike = false;
      animationManager.runAnimation(this);
    }
  }

  /**
   * Updates the game state based on the provided time step and checks if the game is in a static
   * state.
   *
   * <p>
   * This method advances the game state by the given time step and evaluates whether all elements
   * in the game are stationary. It then updates the scene manager with the current game record and
   * gets the current strikeable element. If the game is in a static state (all elements are
   * stationary), the ability to strike is set to true.
   * </p>
   *
   * @param timeStep The time step for updating the animation and game state.
   * @return {@code true} if the game is in a static state (all elements are stationary), otherwise
   * {@code false}.
   */
  public boolean runGameAndCheckStatic(double timeStep) {
    GameRecord gameRecord = gameEngine.update(timeStep);
    boolean staticState = gameRecord.staticState();
    if (staticState) {
      ableToStrike = true;
    }
    sceneManager.update(gameRecord, playerMap, selectedGame);
    return staticState;
  }


  /**
   * Prompts the GameTitleParser to parse for the playable savedgame titles
   *
   * @return a list of the playable saved game titles
   */
  public ObservableList<String> getSavedGameTitles() {
    return gameTitleParser.getSavedGameTitles();
  }


  /**
   * Creates a new game window for the user to play or author a games
   */
  public void createNewWindow() {
    new GameWindow();
  }

  /**
   * Getter for ableToStrike boolean
   *
   * @return true if static state and able to strike, false if not static state and not able to
   * strike
   */
  public boolean getAbleToStrike() {
    return ableToStrike;
  }

  /**
   * Move controllable along x axis
   *
   * @param positive true if along positive x axis, false if along negative x axis
   */
  public void moveControllableX(boolean positive, double minBound, double maxBound) {
    if (animationManager.isRunning()) {
      gameEngine.moveActiveControllableX(positive, minBound, maxBound);
    }
  }

  public void moveControllableY( double minBound, double maxBound) {
    if (animationManager.isRunning()) {
      gameEngine.moveActiveControllableY(minBound, maxBound);
    }
  }

  public List<String> getMods() {
    return gameLoaderView.getMods();
  }


  public void changeMod(String selectedMod) {
    try {
      gameLoaderView.createViewRecord(selectedMod);
      sceneManager.changeMod(gameLoaderView.getViewCollidableInfo());
    } catch (InvalidColorParsingException | InvalidShapeException e) {
      handleException("Parsing Error", "Cannot find corresponding color or mod");
    }
  }


  private void handleException(String title, String message) {
    WARNING.showAlert(this.getScene(), AlertType.ERROR, title, null, message);
  }

  /**
   * Gets the input key for the requested input type
   *
   * @param inputType the type of input being requested
   * @return KeyCode associated with the given input
   */
  public KeyCode getKey(KeyInputType inputType) {
    Map<KeyInputType, String> keyMap = gameLoaderView.getInputKeys();
    try {
      return KeyCode.valueOf(keyMap.getOrDefault(inputType, ""));
    } catch (NullPointerException ignored) {
    }
    return null;
  }

  /**
   * Gets the description associated with the given game
   *
   * @param selectedGame the game to get the description for
   * @return the description for the given game
   */
  public String getDescription(String selectedGame) {
    Properties properties = new Properties();
    try {
      FileInputStream inputStream = new FileInputStream("src/main/resources/view/properties"
          + "/GameDescriptions.properties");
      properties.load(inputStream);
    } catch (IOException e) {
      
      LOGGER.error(e.getMessage());
      WARNING.showAlert(getScene(), AlertType.ERROR, "Game Description Error", null,
          e.getMessage());
    }
    return properties.getProperty(selectedGame, "");
  }

  public Map<String, Boolean> getPlayerPermissions(String gamePath) {
    return databaseController.getPlayerPermissions(gamePath);
  }

  public void managePermissions(String gamePath) {
    sceneManager.createManagePermissionsScene();
  }

  private CompositeElement createCompositeElementFromGameLoader() {
    try {
      List<ViewGameObjectRecord> recordList = gameLoaderView.getViewCollidableInfo();
      return new CompositeElement(recordList);
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
             IllegalAccessException | InvocationTargetException | InvalidImageException e) {
      LOGGER.error(e.getMessage());
      
      return null;
    }
  }


  public void saveGame() {
    GameData gameData = gameLoaderView.getGameData();

    //this is the current game record from engine
    GameRecord currentGameStatus = gameEngine.restoreLastStaticGameRecord();

    //create new game object records
    //new game object records with updated position and visibility
    List<GameObjectProperties> newGameObjectRecords = new ArrayList<>();
    currentGameStatus.gameObjectRecords().forEach((gameObjectRecord) -> {
      //get the initial game obj record corresponding to this current one
      GameObjectProperties initialGameObjRecord = gameLoaderView.getGameObjRecordById(
          gameObjectRecord.id());
      //update visibility
      List<String> properties = initialGameObjRecord.properties();
      properties.remove("visible");
      properties.add(gameObjectRecord.visible() ? "visible" : "invisible");
      //update position
      Position newPos = new Position(gameObjectRecord.x(), gameObjectRecord.y());
      //write new obj record
      GameObjectProperties newGameObj = new GameObjectProperties(gameObjectRecord.id(),
          properties, initialGameObjRecord.mass(), newPos,
          initialGameObjRecord.shape(), initialGameObjRecord.dimension(),
          initialGameObjRecord.color(), initialGameObjRecord.staticFriction(),
          initialGameObjRecord.kineticFriction(), initialGameObjRecord.inclineAngle(),
          initialGameObjRecord.image(), initialGameObjRecord.direction(),
          initialGameObjRecord.inelastic(), initialGameObjRecord.phaser(),
          gameEngine.getScoreableScoreById(gameObjectRecord.id()));
      //add new game obj to the list
      newGameObjectRecords.add(newGameObj);
    });
    //update gameData with the new list
    gameData.setGameObject(newGameObjectRecords);

    //update the variables
    int currTurn = currentGameStatus.turn();
    int currRound = currentGameStatus.round();
    GlobalVariables globalVariables = new GlobalVariables(currTurn, currRound);
    Variables variables = new Variables(globalVariables, gameData.getVariables().get(0).player());
    gameData.setVariables(List.of(variables));

    //update players
    List<ParserPlayer> updatedPlayers = new ArrayList<>();
    currentGameStatus.players().forEach((player) -> {
      //for each player in the game record
      //get the old parser player
      ParserPlayer parserPlayer = gameLoaderView.getParserPlayerById(player.playerId());
      //create a new parserPlayer with the new score
      double totalScore = player.score();
      double objScores = 0;
      for (int id : parserPlayer.myScoreable()) {
        objScores += gameEngine.getScoreableScoreById(id);
      }
      ParserPlayer newParserPlayer = new ParserPlayer(player.playerId(),
          parserPlayer.myStrikeable(), parserPlayer.myScoreable(), parserPlayer.myControllable(),
          totalScore - objScores, player.activeStrikeable());
      updatedPlayers.add(newParserPlayer);
    });
    gameData.setPlayers(updatedPlayers);

    //call builderDirector to serialize gameData into JSON
    BuilderDirector builderDirector = new BuilderDirector();
    builderDirector.writeGame(gameData.getGameName(), gameData,
        RESUME_GAME_DATA_FOLDER);
  }


  public void getGameName() {
    databaseController.getFormattedScoresForLeaderboard(selectedGame,
        !gameLoaderView.getGameData().getRules().rankComparator().equals(INCREASING));
  }


  public void openAddFriends() {
    sceneManager.createAddFriendScene();
  }
}
