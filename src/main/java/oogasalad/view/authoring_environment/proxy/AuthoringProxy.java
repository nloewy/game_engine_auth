package oogasalad.view.authoring_environment.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.shape.Shape;
import oogasalad.model.api.exception.InCompleteRulesAuthoringException;
import oogasalad.view.api.exception.MissingInteractionException;
import oogasalad.view.api.exception.MissingNonControllableTypeException;
import oogasalad.view.authoring_environment.util.GameObjectAttributesContainer;
import oogasalad.view.controller.AuthoringController;
import oogasalad.view.api.enums.CollidableType;

/**
 * AuthoringProxy acts as an intermediary between the authoring environment and the authoring
 * controller, keeping track of data such as interactions, game objects, policies, and commands
 * necessary for creating JSON file for configuring a new game.
 *
 * @author Judy He
 */
public class AuthoringProxy {

  private final Map<String, String> keyPreferences = new HashMap<>();
  private final Map<String, Map<String, List<Integer>>> conditionsCommands = new HashMap<>();
  private final Map<String, String> policies = new HashMap<>();
  private final Map<List<Integer>, Map<String, List<Integer>>> interactionMap = new HashMap<>();
  private final Map<Shape, GameObjectAttributesContainer> gameObjectMap = new HashMap<>();
  private final Map<Integer, Map<CollidableType, List<Integer>>> playersMap = new HashMap<>();
  private String gameName; // POPUP FOR SET GAME NAME
  private String currentScreenTitle;
  private AuthoringController authoringController;
  private int numPlayers = 1;

  /**
   * Adds an interaction for a given list of shapes.
   *
   * @param shapes      The list of shapes involved in the interaction.
   * @param interaction The interaction map specifying the interaction details.
   */
  public void addShapeInteraction(List<Integer> shapes,
      Map<String, List<Integer>> interaction) {
    interactionMap.put(shapes, interaction);
  }

  /**
   * Returns a map of player configurations.
   *
   * @return A map containing player configurations.
   */
  public Map<Integer, Map<CollidableType, List<Integer>>> getPlayers() {
    return playersMap;
  }

  /**
   * Adds a policy without parameters.
   *
   * @param type    The type of policy.
   * @param command The command associated with the policy.
   */
  public void addNoParamPolicies(String type, String command) {
    policies.put(type, command);
  }

  /**
   * Adds conditions and commands with parameters.
   *
   * @param type        The type of the condition or command.
   * @param commandName The name of the command.
   * @param params      The parameters for the command.
   */
  public void addConditionsCommandsWithParam(String type, String commandName,
      List<Integer> params) {
    if (!conditionsCommands.containsKey(type)) {
      conditionsCommands.put(type, new HashMap<>());
    }
    conditionsCommands.get(type).put(commandName, params);
    System.out.println("ALL CONDITIONS:" + conditionsCommands);
  }

  /**
   * Replaces conditions and commands with parameters.
   *
   * @param type        The type of the condition or command.
   * @param commandName The name of the command.
   * @param params      The parameters for the command.
   */
  public void replaceConditionsCommandsWithParam(String type, String commandName,
      List<Integer> params) {
    conditionsCommands.put(type, new HashMap<>());
    conditionsCommands.get(type).put(commandName, params);
    System.out.println("ALL CONDITIONS:" + conditionsCommands);
  }

  /**
   * Removes conditions and commands with parameters.
   *
   * @param type        The type of the condition or command.
   * @param commandName The name of the command.
   */
  public void removeConditionsCommandsWithParam(String type, String commandName) {
    if (!conditionsCommands.containsKey(type)) {
      return;
    }
    conditionsCommands.get(type).remove(commandName);
    System.out.println("ALL CONDITIONS:" + conditionsCommands);
  }

  public void addKeyPreference(String keyType, String keyCode){
    keyPreferences.put(keyType, keyCode);
    System.out.println("CURRENT KEY PREFS: "+keyPreferences);
  }

  public boolean keyAlreadyUsed(String key){
    System.out.println("EXISTING KEYS: "+keyPreferences.values());
    for (String keyCode : keyPreferences.values()){
      if (key.equals(keyCode)){
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the map of game objects.
   *
   * @return A map of game objects and their attributes.
   */
  public Map<Shape, GameObjectAttributesContainer> getGameObjectMap() {
    return gameObjectMap;
  }

  /**
   * Sets a game object with the specified shape and attributes.
   *
   * @param shape                         The shape representing the game object.
   * @param gameObjectAttributesContainer The attributes of the game object.
   */
  public void setGameObject(Shape shape,
      GameObjectAttributesContainer gameObjectAttributesContainer) {
    this.gameObjectMap.put(shape, gameObjectAttributesContainer);
  }

  /**
   * Completes the authoring process by writing rules, players, variables, and game objects.
   *
   * @throws MissingInteractionException         If there is a missing interaction.
   * @throws MissingNonControllableTypeException If there is a missing non-controllable type.
   */
  public void completeAuthoring()
      throws MissingInteractionException, MissingNonControllableTypeException {
    try {
      authoringController.writeRules(conditionsCommands, policies);
      authoringController.writePlayers(playersMap);
      authoringController.writeVariables();
      authoringController.writeGameObjects(gameObjectMap);
      authoringController.writeKeyPreferences(keyPreferences);
      boolean saveGameSuccess = authoringController.submitGame(gameName);
      if (saveGameSuccess) {
        showSuceessMessage("Game successfully saved!");
      } else {
        showSaveGameError("Save game failed :(");
      }
    } catch (InCompleteRulesAuthoringException e) {
      showSaveGameError(e.getMessage());
    }
  }


  private void showSaveGameError(String errorMessage) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Save Game Error");
    alert.setHeaderText(null);
    alert.setContentText(errorMessage);
    alert.showAndWait();
  }

  private void showSuceessMessage(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Save Game Success");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Updates the authoring screen.
   */
  public void updateScreen() {
    authoringController.updateAuthoringScreen();
  }

  /**
   * Returns the game name.
   *
   * @return The name of the game.
   */
  public String getGameName() {
    return gameName;
  }

  /**
   * Sets the game name.
   *
   * @param gameName The name of the game.
   */
  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  /**
   * Returns the current screen title.
   *
   * @return The current screen title.
   */
  public String getCurrentScreenTitle() {
    return currentScreenTitle;
  }

  /**
   * Sets the current screen title.
   *
   * @param currentScreenTitle The current screen title.
   */
  public void setCurrentScreenTitle(String currentScreenTitle) {
    this.currentScreenTitle = currentScreenTitle;
  }

  /**
   * Sets the authoring controller.
   *
   * @param authoringController The authoring controller.
   */
  public void setAuthoringController(
      AuthoringController authoringController) {
    this.authoringController = authoringController;
  }

  /**
   * Returns the number of players.
   *
   * @return The number of players.
   */
  public int getNumPlayers() {
    return numPlayers;
  }

  /**
   * Increases the number of players.
   */
  public void increaseNumPlayers() {
    numPlayers++;
  }

  /**
   * Decreases the number of players.
   */
  public void decreaseNumPlayers() {
    numPlayers--;
  }

  /**
   * Returns the current player ID.
   *
   * @return The current player ID.
   */
  public int getCurrentPlayerId() {
    return numPlayers - 1;
  }

  /**
   * Removes an object from all players' game object lists.
   *
   * @param shapeId The ID of the shape to remove.
   */
  public void removeObjectFromPlayersAllLists(Integer shapeId) {
    removeCollidableFromAllPlayers(CollidableType.STRIKABLE, shapeId);
    removeCollidableFromAllPlayers(CollidableType.CONTROLLABLE, shapeId);
    removeCollidableFromAllPlayers(CollidableType.SCOREABLE, shapeId);
  }

  /**
   * Removes a collidable object from all players.
   *
   * @param collidableType The type of collidable object.
   * @param shapeId        The ID of the shape to remove.
   */
  public void removeCollidableFromAllPlayers(CollidableType collidableType, Integer shapeId) {
    for (Integer player : playersMap.keySet()) {
      removeCollidableFromPlayer(player, collidableType, shapeId);
    }
  }

  /**
   * Removes a collidable object from a specific player.
   *
   * @param playerId       The ID of the player.
   * @param collidableType The type of collidable object.
   * @param shapeId        The ID of the shape to remove.
   */
  public void removeCollidableFromPlayer(int playerId, CollidableType collidableType,
      Integer shapeId) {
    if (playersMap.get(playerId).get(collidableType).contains(shapeId)) {
      playersMap.get(playerId).get(collidableType).remove(shapeId);
    }
  }

  /**
   * Adds a new player to the game.
   */
  public void addNewPlayer() {
    playersMap.putIfAbsent(getCurrentPlayerId(), new HashMap<>());
    playersMap.get(getCurrentPlayerId()).putIfAbsent(CollidableType.STRIKABLE, new ArrayList<>());
    playersMap.get(getCurrentPlayerId())
        .putIfAbsent(CollidableType.CONTROLLABLE, new ArrayList<>());
    playersMap.get(getCurrentPlayerId()).putIfAbsent(CollidableType.SCOREABLE, new ArrayList<>());
  }

  /**
   * Removes the most recently added player from the game.
   */
  public void removeMostRecentAddedPlayer() {
    playersMap.remove(getCurrentPlayerId());
  }

  /**
   * Adds a collidable object to a specific player.
   *
   * @param selectedPlayerId   The ID of the player.
   * @param collidableType     The type of collidable object.
   * @param shapeId            The ID of the shape to add.
   * @param isControllable     Whether the object is controllable.
   * @param controllableXSpeed The controllable object's speed on the X axis (if applicable).
   * @param controllableYSpeed The controllable object's speed on the Y axis (if applicable).
   */
  public void addCollidableToPlayer(int selectedPlayerId, CollidableType collidableType,
      Integer shapeId, boolean isControllable, int controllableXSpeed, int controllableYSpeed) {
    if (selectedPlayerId >= 0) {
      if (isControllable) {
        playersMap.get(selectedPlayerId)
            .put(collidableType, List.of(shapeId, controllableXSpeed, controllableYSpeed));
      } else if (!playersMap.get(selectedPlayerId).get(collidableType).contains(shapeId)) {
        playersMap.get(selectedPlayerId).get(collidableType).add(shapeId);
      }
    }
  }


}
