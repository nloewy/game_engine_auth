package oogasalad.view.AuthoringScreens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import oogasalad.view.Controlling.AuthoringController;

/**
 * Class to represent the screen in which the user customizes the background of their unique game
 *
 * @author Jordan Haytaian, Doga Ozmen
 */
public class SurfaceSelectionScreen extends AuthoringScreen {

  private Rectangle background;

  public SurfaceSelectionScreen(AuthoringController controller, StackPane authoringBox) {
    super(controller, authoringBox);
  }

  /**
   * Creates the scene for configuring the background
   */
  void createScene() {
    root = new StackPane();
    createAuthoringBox();
    createShapeDisplayOptionBox();
    createNextButton();
    scene = new Scene(root, screenWidth, screenHeight);
  }

  /**
   * When next button is clicked, controller is prompted to start the next selection process
   */
  void endSelection() {
    controller.startNextSelection(ImageType.BACKGROUND, authoringBox);
  }

  /**
   * Returns background image type indicating that user is configuring the background
   *
   * @return enum to represent background image type
   */
  ImageType getImageType() {
    return ImageType.BACKGROUND;
  }

  private void createAuthoringBox() {
    authoringBox.setMaxSize(authoringBoxWidth, authoringBoxHeight);
    StackPane.setAlignment(authoringBox, Pos.TOP_LEFT);
    StackPane.setMargin(authoringBox, new Insets(50, 0, 0, 50));

    background = new Rectangle(authoringBoxWidth, authoringBoxHeight);
    background.setStroke(Color.BLACK);
    background.setFill(Color.WHITE);
    background.setStrokeWidth(10);
    StackPane.setAlignment(background, Pos.TOP_LEFT);
    selectedShape = background;

    authoringBox.getChildren().add(background);
    root.getChildren().add(authoringBox);
  }
}
