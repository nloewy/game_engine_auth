package oogasalad.view.scene_management.scene_element.scene_element_handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import oogasalad.model.gameparser.GameLoader;
import oogasalad.view.Warning;
import oogasalad.view.api.enums.SceneElementEvent;
import oogasalad.view.controller.DatabaseController;
import oogasalad.view.controller.GameController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * LoadGameEventHandler handles events related to loading games in the application. It creates event
 * handlers for nodes based on the specified events and provides appropriate actions for starting
 * new or saved games.
 */
public class LoadGameEventHandler {

  private final String newGameDir = "data/playable_games/";
  private final String resumeGameDir = "data/resume_game/";
  private final GameController gameController;
  private final DatabaseController databaseController;
  private final DatabaseHandler handler;
  private Map<SceneElementEvent, Callable<ObservableList<String>>> eventMap;
  private Map<SceneElementEvent, String> dirPathMap;
  private Map<SceneElementEvent, BiConsumer<String, String>> handleSelectGameMap;
  private static final Logger LOGGER = LogManager.getLogger(LoadGameEventHandler.class);
  private static final Warning WARNING = new Warning();

  /**
   * Constructs a LoadGameEventHandler with the specified GameController.
   *
   * @param gameController The GameController used to handle game-related events.
   */
  public LoadGameEventHandler(GameController gameController,
      DatabaseController databaseController, DatabaseHandler handler) {
    this.gameController = gameController;
    this.databaseController = databaseController;
    this.handler = handler;
    createEventMap();
    createDirPathMap();
    createSelectGameMap();
  }

  /**
   * Creates an event handler for the specified node and event type.
   *
   * @param node  The node to which the event handler will be attached.
   * @param event The event type as a string.
   */
  public void createElementHandler(Node node, String event) {
    Callable<ObservableList<String>> callable = eventMap.get(SceneElementEvent.valueOf(event));
    BiConsumer<String, String> biConsumer = handleSelectGameMap.get(
        SceneElementEvent.valueOf(event));
    String dirPath = dirPathMap.get(SceneElementEvent.valueOf(event));
    createStartGameHandler(node, callable, biConsumer, dirPath);
  }

  private void createEventMap() {
    eventMap = new HashMap<>();
    eventMap.put(SceneElementEvent.START_NEW_GAME, databaseController::getNewGameTitles);
    eventMap.put(SceneElementEvent.START_SAVED_GAME, gameController::getSavedGameTitles);
    eventMap.put(SceneElementEvent.MANAGE_GAME, databaseController::getManageableGames);
  }

  private void createSelectGameMap() {
    handleSelectGameMap = new HashMap<>();
    handleSelectGameMap.put(SceneElementEvent.START_NEW_GAME, this::handleSelectGame);
    handleSelectGameMap.put(SceneElementEvent.START_SAVED_GAME, this::handleSelectGame);
    handleSelectGameMap.put(SceneElementEvent.MANAGE_GAME, this::handleManageGame);
  }


  private void createDirPathMap() {
    dirPathMap = new HashMap<>();
    dirPathMap.put(SceneElementEvent.START_NEW_GAME, newGameDir);
    dirPathMap.put(SceneElementEvent.START_SAVED_GAME, resumeGameDir);
  }

  private void createStartGameHandler(Node node, Callable<ObservableList<String>> getItemsCallable,
      BiConsumer<String, String> handleSelectionBiConsumer,
      String dirPath) {
    try {
      ListView<String> gameList = (ListView<String>) node;
      ObservableList<String> items = getItemsCallable.call();
      gameList.setItems(items);
      gameList.setCellFactory(lv -> new ListCell<String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(null);
            setTooltip(null);
            setOnMouseClicked(null);
          } else {
            setText(item);
            Tooltip tooltip = new Tooltip(gameController.getDescription(item));
            setTooltip(tooltip);

            setOnMouseClicked(event -> handleSelectionBiConsumer.accept(dirPath, item));
          }
        }
      });
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      //WARNING.showAlert(AlertType.ERROR, "Start Game Error", null, e.getMessage());
    }
  }

  private void handleSelectGame(String dirPath, String gameTitle) {
    if (gameTitle != null) {
      gameController.startGamePlay(dirPath + gameTitle);
    }
  }

  private void handleManageGame(String dirPath, String gameTitle) {
    if (gameTitle != null) {
      handler.setGame(gameTitle);
      gameController.managePermissions(gameTitle);
    }
  }
}
