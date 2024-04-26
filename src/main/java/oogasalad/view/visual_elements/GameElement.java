package oogasalad.view.visual_elements;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import oogasalad.model.api.GameObjectRecord;
import oogasalad.model.api.ViewGameObjectRecord;
import oogasalad.model.api.exception.InvalidImageException;
import oogasalad.model.api.exception.InvalidShapeException;

/**
 * Class for a single View Element for the Game engine.
 */
public class GameElement implements VisualElement {

  private final Node myNode;
  private final int id;

  public GameElement(ViewGameObjectRecord viewData)
      throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
      IllegalAccessException, InvocationTargetException {
    id = viewData.id();
    myNode = makeShape(viewData);
    myNode.setTranslateX(viewData.startXpos());
    myNode.setTranslateY(viewData.startYpos());
  }

  private Node makeShape(ViewGameObjectRecord data)
      throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
      IllegalAccessException, InvocationTargetException {
    String className = data.shape();
    Class<?> classObj = Class.forName(className);
    Object obj = classObj.getDeclaredConstructor(double.class, double.class)
        .newInstance(data.width(), data.height());
    Shape shape = (Shape) obj;

    if (data.image().isEmpty()) {
      List<Integer> rgb = data.color();
      Color color = Color.rgb(rgb.get(0), rgb.get(1), rgb.get(2));
      shape.setFill(color);
    } else {
      Path imgPath = Paths.get(data.image());
      Image image = new Image(imgPath.toUri().toString());
      shape.setFill(new ImagePattern(image));
    }

    return shape;
  }


  /**
   * getter for element id
   *
   * @return element id
   */
  public int getId() {
    return id;
  }

  /**
   * Updates the Element properties.
   *
   * @param model This Element's corresponding model object.
   */
  @Override
  public void update(GameObjectRecord model) {
    myNode.setTranslateX(model.x());
    myNode.setTranslateY(model.y());
    myNode.setVisible(model.visible());
  }

  /**
   * Returns the Node housed by this Element.
   *
   * @return Node  This Element's Node.
   */
  @Override
  public Node getNode() {
    return myNode;
  }
}
