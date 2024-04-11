package oogasalad.view.AuthoringScreens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import oogasalad.view.Controlling.AuthoringController;

/**
 * Class to represent the screen in which user places and customizes obstacle objects in their
 * unique game
 *
 * @author Jordan Haytaian, Doga Ozmen
 */
public class NonControllableElementSelection extends AuthoringScreen {

  private ComboBox<NonControllableType> obstacleTypeComboBox;

  private Map<Shape, NonControllableType> obstacleTypeMap;


  public NonControllableElementSelection(AuthoringController controller, StackPane authoringBox,
      Map<Shape, List<Double>> posMap,
      Map<Shape, NonControllableType> nonControllableMap, List<Shape> controllableList,
      Map<Shape, String> imageMap) {
    super(controller, authoringBox, posMap, nonControllableMap, controllableList, imageMap);
    obstacleTypeMap = new HashMap<>();
  }

  /**
   * Creates the scene including the previously selected background
   */
  void createScene() {
    root = new AnchorPane();
    createTitle("NonControllable Selection");
    root.getChildren().add(authoringBox);
    addElements();
    createSizeAndAngleSliders();
    createShapeDisplayOptionBox();
    createDraggableShapeTemplates();
    createTypeDropDown();
    createTransitionButton("Next");
    scene = new Scene(root, screenWidth, screenHeight);
  }

  /**
   * Updates object type to reflect type of currently selected object
   */
  void updateOptionSelections() {
    NonControllableType type = obstacleTypeMap.getOrDefault(selectedShape, null);
    obstacleTypeComboBox.setValue(type);
  }

  void createTypeDropDown() {
    obstacleTypeComboBox = new ComboBox<>();
    obstacleTypeComboBox.getItems()
        .addAll(NonControllableType.SURFACE, NonControllableType.MOVABLE);
    obstacleTypeComboBox.setPromptText("Select Obstacle Type");
    AnchorPane.setRightAnchor(obstacleTypeComboBox, 50.0);
    AnchorPane.setBottomAnchor(obstacleTypeComboBox, 300.0);
    obstacleTypeComboBox.setPrefSize(200, 100);
    root.getChildren().add(obstacleTypeComboBox);

    // Listen for changes and update the map
    obstacleTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
      if (selectedShape != null && newVal != null) {
        obstacleTypeMap.put(selectedShape, newVal);
      }
    });
  }

  private boolean allSelectionsMade() {
    for (Shape shape : selectableShapes) {
      Bounds shapeBounds = shape.getBoundsInParent();
      Bounds authoringBoxBounds = authoringBox.getBoundsInParent();

      if (authoringBoxBounds.contains(shapeBounds)) {
        if (!obstacleTypeMap.containsKey(shape)) {
          return false; // Found an obstacle without an assigned type
        }
      }
    }
    return true; // All obstacles have an assigned type
  }

  /**
   * When the next button is clicked, controller is prompted to start the next selection process
   */
  @Override
  void endSelection() {
    if (allSelectionsMade()) {
      addNewSelectionsToAuthoringBox();

      for (Shape shape : selectableShapes) {
        Bounds shapeBounds = shape.getBoundsInParent();
        Bounds authoringBoxBounds = authoringBox.getBoundsInParent();

        if (authoringBoxBounds.contains(shapeBounds)) {
          NonControllableType type = obstacleTypeMap.get(shape);
          nonControllableMap.put(shape, type);
        }
      }

      controller.startNextSelection(ImageType.NONCONTROLLABLE_ELEMENT,
          authoringBox, posMap, nonControllableMap,
          controllableList, imageMap); // Adjust NEXT_TYPE to whatever comes next
    } else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText("Assign A Type To All Elements Before Continuing");
      alert.showAndWait();
    }
  }


  /**
   * Returns obstacle image type indicating that user is placing obstacle objects
   *
   * @return enum to represent goal image type
   */
  ImageType getImageType() {
    return ImageType.NONCONTROLLABLE_ELEMENT;
  }
}
