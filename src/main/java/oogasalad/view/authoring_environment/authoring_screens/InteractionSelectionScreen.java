package oogasalad.view.authoring_environment.authoring_screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.view.controller.AuthoringController;

public class InteractionSelectionScreen extends AuthoringScreen {

  private final Map<List<Shape>, Map<InteractionType, List<Integer>>> interactionMap = new HashMap<>();
  private final Map<Shape, List<Double>> posMap = new HashMap<>();
  private TextField pointPrompt;
  private CheckBox advanceTurnCheckBox;
  private CheckBox resetCheckBox;
  private CheckBox changeSpeedCheckBox;
  private final Set<Shape> clickedShapes = new HashSet<>();
  private TextField gameNameTextField;
  private Stage gameNameStage;
  private Button submitGameNameButton;


  public InteractionSelectionScreen(AuthoringController controller, StackPane authoringBox,
      Map<Shape, List<Double>> posMap,
      Map<Shape, NonStrikeableType> nonStrikeableTypeMap, List<Shape> strikeableList,
      Map<Shape, String> imageMap) {
    super(controller, authoringBox, posMap, nonStrikeableTypeMap, strikeableList, imageMap);
  }

  /**
   * Creates the scene including the previously selected background
   */
  void createScene() {
    root = new AnchorPane();
    createTitle("Interaction Selection");
    root.getChildren().add(authoringBox);
    addElements();
    createInteractionOptions();
    setUpShapes();
    createTransitionButton("Submit");
    scene = new Scene(root, screenWidth, screenHeight);
  }

  void updateOptionSelections() {
    if (clickedShapes.size() != 2) {
      pointPrompt.setText("");
      pointPrompt.setEditable(false);
      advanceTurnCheckBox.setSelected(false);
      advanceTurnCheckBox.setDisable(true);
      resetCheckBox.setSelected(false);
      resetCheckBox.setDisable(true);
      changeSpeedCheckBox.setSelected(false);
      changeSpeedCheckBox.setDisable(true);
    } else {
      updatePointPrompt();
      updateCheckBoxes();
      if (canInteract()) {
        pointPrompt.setEditable(true);
        advanceTurnCheckBox.setDisable(false);
        resetCheckBox.setDisable(false);
        if (slowIsOption()) {
          changeSpeedCheckBox.setDisable(false);
        }
      }
    }
  }

  ImageType getImageType() {
    return null;
  }

  void endSelection() {
    for (Shape shape : strikeableList) {
      List<Double> posList = new ArrayList<>();
      posList.add(shape.localToScene(shape.getBoundsInLocal()).getMinX());
      posList.add(shape.localToScene(shape.getBoundsInLocal()).getMinY());
      posMap.put(shape, posList);
    }
    for (Shape shape : nonStrikeableMap.keySet()) {
      List<Double> posList = new ArrayList<>();
      posList.add(shape.localToScene(shape.getBoundsInLocal()).getMinX());
      posList.add(shape.localToScene(shape.getBoundsInLocal()).getMinY());
      posMap.put(shape, posList);
    }
    showGameNamePopup();
  }

  private void showGameNamePopup() {
    if (gameNameStage == null) {
      gameNameStage = new Stage();
      gameNameStage.initModality(Modality.APPLICATION_MODAL);
      gameNameStage.setTitle("Enter Game Name");

      VBox vbox = new VBox();
      gameNameTextField = new TextField();
      gameNameTextField.setPromptText("Enter game name...");
      vbox.getChildren().addAll(gameNameTextField, submitGameNameButton());

      Scene scene = new Scene(vbox, 300, 100);
      gameNameStage.setScene(scene);
    }
    gameNameStage.showAndWait();
  }

  private Button submitGameNameButton() {
    if (submitGameNameButton == null) {
      submitGameNameButton = new Button("Submit");
      submitGameNameButton.setOnAction(e -> {
        gameNameStage.close();
        String gameName = gameNameTextField.getText();
        controller.endAuthoring(gameName, interactionMap, strikeableList, nonStrikeableMap,
            imageMap, posMap);
      });
    }
    return submitGameNameButton;
  }

  private void createInteractionOptions() {
    createCheckBoxes();
    createPointOptions();
  }

  private void createCheckBoxes() {
    advanceTurnCheckBox = new CheckBox("Advance");
    resetCheckBox = new CheckBox("Reset");
    changeSpeedCheckBox = new CheckBox("Change Speed");

    advanceTurnCheckBox.setDisable(true);
    resetCheckBox.setDisable(true);
    changeSpeedCheckBox.setDisable(true);

    advanceTurnCheckBox.setPrefSize(150, 150);
    AnchorPane.setRightAnchor(advanceTurnCheckBox, 75.0);
    AnchorPane.setBottomAnchor(advanceTurnCheckBox, 300.0);
    createAdvanceHandler();

    resetCheckBox.setPrefSize(150, 150);
    AnchorPane.setRightAnchor(resetCheckBox, 75.0);
    AnchorPane.setBottomAnchor(resetCheckBox, 400.0);
    createResetHandler();

    changeSpeedCheckBox.setPrefSize(150, 150);
    AnchorPane.setRightAnchor(changeSpeedCheckBox, 75.0);
    AnchorPane.setBottomAnchor(changeSpeedCheckBox, 500.0);
    createChangeHandler();

    root.getChildren().addAll(advanceTurnCheckBox, resetCheckBox, changeSpeedCheckBox);
  }

  private void createAdvanceHandler() {
    advanceTurnCheckBox.setOnMouseClicked(e -> {
      resetCheckBox.setSelected(false);
      changeSpeedCheckBox.setSelected(false);

      for (List<Shape> list : interactionMap.keySet()) {
        if (list.containsAll(clickedShapes)) {
          Map<InteractionType, List<Integer>> currentInteractions = interactionMap.get(list);
          currentInteractions.remove(InteractionType.RESET);
          currentInteractions.remove(InteractionType.CHANGE_SPEED);
          currentInteractions.put(InteractionType.ADVANCE, List.of( -1));
          return;
        }
      }
      List<Shape> shapeList = new ArrayList<>(clickedShapes);
      Map<InteractionType, List<Integer>> currentInteractions = new HashMap<>();
      currentInteractions.put(InteractionType.ADVANCE, List.of( -1));
      interactionMap.put(shapeList, currentInteractions);
    });
  }

  private void createResetHandler() {
    resetCheckBox.setOnMouseClicked(e -> {
      advanceTurnCheckBox.setSelected(false);
      changeSpeedCheckBox.setSelected(false);

      for (List<Shape> list : interactionMap.keySet()) {
        if (list.containsAll(clickedShapes)) {
          Map<InteractionType, List<Integer>> currentInteractions = interactionMap.get(list);
          currentInteractions.remove(InteractionType.ADVANCE);
          currentInteractions.remove(InteractionType.CHANGE_SPEED);
          currentInteractions.put(InteractionType.RESET, List.of( -1));
          return;
        }
      }
      List<Shape> shapeList = new ArrayList<>(clickedShapes);
      Map<InteractionType, List<Integer>> currentInteractions = new HashMap<>();
      currentInteractions.put(InteractionType.RESET, List.of( -1));
      interactionMap.put(shapeList, currentInteractions);
    });
  }

  private void createChangeHandler() {
    changeSpeedCheckBox.setOnMouseClicked(e -> {
      advanceTurnCheckBox.setSelected(false);
      resetCheckBox.setSelected(false);

      for (List<Shape> list : interactionMap.keySet()) {
        if (list.containsAll(clickedShapes)) {
          Map<InteractionType, List<Integer>> currentInteractions = interactionMap.get(list);
          currentInteractions.remove(InteractionType.RESET);
          currentInteractions.remove(InteractionType.ADVANCE);
          currentInteractions.put(InteractionType.CHANGE_SPEED, List.of(-1));
          return;
        }
        List<Shape> shapeList = new ArrayList<>(clickedShapes);
        Map<InteractionType, List<Integer>> currentInteractions = new HashMap<>();
        currentInteractions.put(InteractionType.CHANGE_SPEED, List.of( -1));
        interactionMap.put(shapeList, currentInteractions);
      }
    });
  }

  private void createPointOptions() {
    pointPrompt = new TextField();
    pointPrompt.setEditable(false);
    pointPrompt.setPrefSize(75, 75);
    createPointsScoredHandler();

    Label label = new Label("Points Scored on Collision");
    AnchorPane.setRightAnchor(label, 75.0);
    AnchorPane.setBottomAnchor(label, 250.0);

    HBox pointPromptContainer = new HBox(pointPrompt);
    pointPromptContainer.setMaxSize(75, 75);
    AnchorPane.setRightAnchor(pointPromptContainer, 100.0);
    AnchorPane.setBottomAnchor(pointPromptContainer, 150.0);

    root.getChildren().addAll(label, pointPromptContainer);
  }

  private void createPointsScoredHandler() {
    pointPrompt.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        String pointsText = pointPrompt.getText();
        try {
          Integer points = Integer.parseInt(pointsText);
          for (List<Shape> list : interactionMap.keySet()) {
            if (list.containsAll(clickedShapes)) {
              Map<InteractionType, List<Integer>> currentInteractions = interactionMap.get(list);
              currentInteractions.put(InteractionType.SCORE, List.of( 1, points));
              return;
            }
          }

          List<Shape> shapeList = new ArrayList<>(clickedShapes);
          Map<InteractionType, List<Integer>> currentInteractions = new HashMap<>();
          currentInteractions.put(InteractionType.SCORE, List.of( 1,  points));
          interactionMap.put(shapeList, currentInteractions);

        } catch (NumberFormatException e) {
          Alert alert = new Alert(AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText(null);
          alert.setContentText("Please Enter an Integer");
          alert.showAndWait();
        }
      }
    });
  }

  private void updatePointPrompt() {
    pointPrompt.setText("");
    for (List<Shape> list : interactionMap.keySet()) {
      if (list.containsAll(clickedShapes)) {
        Map<InteractionType, List<Integer>> currentInteractions = interactionMap.get(list);
        if (currentInteractions.containsKey(InteractionType.SCORE)) {
//          Integer points = currentInteractions.get(InteractionType.SCORE).get(1);
          Integer points = currentInteractions.get(InteractionType.SCORE).get(1);
          pointPrompt.setText(points.toString());
        }
      }
    }
  }

  private void updateCheckBoxes() {
    for (List<Shape> list : interactionMap.keySet()) {
      if (list.containsAll(clickedShapes)) {
        Map<InteractionType, List<Integer>> currentInteractions = interactionMap.get(list);
        if (currentInteractions.containsKey(InteractionType.ADVANCE)) {
          advanceTurnCheckBox.setSelected(true);
        } else if (currentInteractions.containsKey(InteractionType.RESET)) {
          resetCheckBox.setSelected(true);
        } else if (currentInteractions.containsKey(InteractionType.CHANGE_SPEED)) {
          changeSpeedCheckBox.setSelected(true);
        }
      }
    }
  }

  private void makeMultiSelectable(Shape shape) {
    selectableShapes.add(shape);
    shape.setOnMouseClicked(event -> {
      if (clickedShapes.size() <= 2) {
        if (shape.getStrokeWidth() == 3) { //if already selected, unselected
          shape.setStrokeWidth(0);
          clickedShapes.remove(shape);
        } else if (clickedShapes.size() < 2) {
          clickedShapes.add(shape);
          shape.setStroke(Color.YELLOW);
          shape.setStrokeWidth(3);
        }
        updateOptionSelections();
      }
    });
  }


  private void setUpShapes() {
    for (Shape shape : strikeableList) {
      makeMultiSelectable(shape);
    }
    for (Shape shape : nonStrikeableMap.keySet()) {
      makeMultiSelectable(shape);
    }
  }

  private boolean canInteract() {
    int numStrikeables = getNumClickedStrikeables();
    if (numStrikeables > 0) {
      if (numStrikeables == 2) {
        return true;
      }
      return getNumClickedNonStrikeables() > 0;
    }
    return false;
  }

  private int getNumClickedStrikeables() {
    int count = 0;
    for (Shape shape : clickedShapes) {
      if (strikeableList.contains(shape)) {
        count++;
      }
    }
    return count;
  }

  private int getNumClickedNonStrikeables() {
    int count = 0;
    for (Shape shape : clickedShapes) {
      if (nonStrikeableMap.containsKey(shape)) {
        count++;
      }
    }
    return count;
  }

  private boolean slowIsOption() {
    for (Shape shape : clickedShapes) {
      if (nonStrikeableMap.getOrDefault(shape, NonStrikeableType.COLLIDABLE)
          == NonStrikeableType.SURFACE) {
        return true;
      }
    }
    return false;
  }

}
