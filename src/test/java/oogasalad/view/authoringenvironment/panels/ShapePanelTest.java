package oogasalad.view.authoringenvironment.panels;

import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import oogasalad.view.api.authoring.AuthoringFactory;
import oogasalad.view.authoring_environment.factories.DefaultUIElementFactory;
import oogasalad.view.authoring_environment.panels.ShapePanel;
import oogasalad.view.authoring_environment.proxy.AuthoringProxy;
import oogasalad.view.authoring_environment.proxy.ShapeProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class ShapePanelTest extends DukeApplicationTest {

  private ShapePanel shapePanel;
  private ShapeProxy mockShapeProxy;
  private AuthoringProxy mockAuthoringProxy;
  private AuthoringFactory testAuthoringFactory;
  private StackPane canvas;
  private AnchorPane rootPane;
  private AnchorPane containerPane;

  @BeforeEach
  public void setUp() {
    Platform.runLater(() -> {
      try {
        mockShapeProxy = mock(ShapeProxy.class);
        mockAuthoringProxy = mock(AuthoringProxy.class);
        testAuthoringFactory = new TestAuthoringFactory(); // Use the concrete test factory

        canvas = new StackPane();
        rootPane = new AnchorPane();
        containerPane = new AnchorPane();

        shapePanel = new ShapePanel(testAuthoringFactory, mockShapeProxy, mockAuthoringProxy, canvas, rootPane, containerPane);

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
  public void testInitialization() {
    assertNotNull(shapePanel, "ShapePanel should be initialized.");
    assertFalse(containerPane.getChildren().isEmpty(), "Container pane should contain elements after initialization.");
  }

}