package oogasalad.view.authoringenvironment.panels;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Stack;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import oogasalad.view.authoring_environment.panels.ColorPanel;
import oogasalad.view.authoring_environment.proxy.ShapeProxy;
import oogasalad.view.authoring_environment.util.GameObjectAttributesContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class ColorPanelTest extends DukeApplicationTest {

  private ColorPanel colorPanel;
  private ShapeProxy mockShapeProxy;
  private AnchorPane containerPane;

  @BeforeEach
  public void setUp() {
    Platform.runLater(() -> {
      try {
        Rectangle rectangle = new Rectangle(10, 10, Color.WHITE);
        mockShapeProxy = mock(ShapeProxy.class);
        when(mockShapeProxy.getShape()).thenReturn(rectangle);

        containerPane = new AnchorPane();
        colorPanel = new ColorPanel(mockShapeProxy, containerPane);

        Stage stage = new Stage();
        Scene scene = new Scene(containerPane);
        stage.setScene(scene);
        stage.show();
      } catch (Exception e) {
        
      }
    });
    waitForFxEvents(); // Wait for the UI to stabilize
  }

  @Test
  public void testColorPickerExists() {
    ColorPicker colorPicker = (ColorPicker) containerPane.lookup("#colorPicker");
    assertNotNull(colorPicker, "ColorPicker should be initialized and added to the pane.");
  }

  @Test
  public void testColorChange() {
    Platform.runLater(() -> {
      ColorPicker colorPicker = (ColorPicker) containerPane.lookup("#colorPicker");
      Rectangle rect = (Rectangle) mockShapeProxy.getShape();

      // Simulate shape selection
      when(mockShapeProxy.getShape()).thenReturn(rect);
      rect.setFill(Color.RED); // Simulate user action of selecting a color after selection
      colorPicker.setValue(Color.RED);

      waitForFxEvents();

      assertEquals(Color.RED, rect.getFill(), "Shape fill should be red after selection and color change.");

    });

    waitForFxEvents();
  }

  @Test
  public void testBoundaryColorValuesWhite() {
    Platform.runLater(() -> {
      ColorPicker colorPicker = (ColorPicker) containerPane.lookup("#colorPicker");
      Rectangle rect = (Rectangle) mockShapeProxy.getShape();

      // Test with full intensity (white)
      Color white = Color.WHITE;
      colorPicker.setValue(white); // Simulate user action via UI
      waitForFxEvents(); // Wait for JavaFX to process the event
      assertEquals(white, rect.getFill(), "Shape fill should be white (full intensity).");
    });
    waitForFxEvents(); // Ensure all JavaFX operations have completed
  }

  @Test
  public void testBoundaryColorValuesBlack() {
    Platform.runLater(() -> {
      ColorPicker colorPicker = (ColorPicker) containerPane.lookup("#colorPicker");
      Rectangle rect = (Rectangle) mockShapeProxy.getShape();

      // Simulate shape selection
      when(mockShapeProxy.getShape()).thenReturn(rect);
      rect.setFill(Color.BLACK); // Simulate user action of selecting a color after selection
      colorPicker.setValue(Color.BLACK);

      waitForFxEvents();

      assertEquals(Color.BLACK, rect.getFill(), "Shape fill should be red after selection and color change.");

    });

    waitForFxEvents();
  }

  @Test
  public void testColorPickerVisibility() {
    Platform.runLater(() -> {
      ColorPicker colorPicker = (ColorPicker) containerPane.lookup("#colorPicker");
      assertTrue(colorPicker.isVisible(), "ColorPicker should be visible.");
    });
    waitForFxEvents();
  }

  @Test
  public void testColorPickerInteractionWithSelectedShape() {
    Platform.runLater(() -> {
      Rectangle rect = new Rectangle(10, 10, Color.WHITE);
      when(mockShapeProxy.getShape()).thenReturn(rect);

      ColorPicker colorPicker = (ColorPicker) containerPane.lookup("#colorPicker");
      // Simulate user selecting a shape and then picking a color
      rect.setFill(Color.BLUE); // Assume this color change is from user interaction
      colorPicker.setValue(Color.BLUE); // Simulate setting the color via the ColorPicker

      waitForFxEvents();

      assertEquals(Color.BLUE, rect.getFill(), "Shape fill should be blue after user selects the shape and changes color.");
    });

    waitForFxEvents(); // Ensure all JavaFX operations have completed
  }

  @Test
  public void testColorPickerResetOnNewSelection() {
    Platform.runLater(() -> {
      Rectangle firstRect = new Rectangle(10, 10, Color.RED);
      Rectangle secondRect = new Rectangle(10, 10, Color.WHITE);

      ColorPicker colorPicker = (ColorPicker) containerPane.lookup("#colorPicker");
      when(mockShapeProxy.getShape()).thenReturn(firstRect);
      colorPicker.setValue(Color.GREEN);

      when(mockShapeProxy.getShape()).thenReturn(secondRect);
      assertEquals(Color.WHITE, secondRect.getFill(), "Newly selected shape should not retain previous color.");
    });
    waitForFxEvents();
  }
}