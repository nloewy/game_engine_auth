package oogasalad.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import oogasalad.model.api.ViewGameObjectRecord;
import oogasalad.model.api.data.GameObjectShape;
import oogasalad.model.gameparser.GameLoaderView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameLoaderViewTest {

  @BeforeEach
  public void setup() {
  }

  @Test
  public void createCollidableRecordTest() {
    GameLoaderView loaderView = new GameLoaderView("testSinglePlayerMiniGolf");
    List<ViewGameObjectRecord> collidableRecords = loaderView.getViewCollidableInfo();
    for (ViewGameObjectRecord record : collidableRecords) {
      if (record.id() == 2 || record.id() == 3) {
        assertEquals(GameObjectShape.ELLIPSE, record.shape());
      } else {
        assertEquals(GameObjectShape.RECTANGLE, record.shape());
      }
    }
  }

  @Test
  public void wrongRgbValueTest() {
    GameLoaderView loaderView = new GameLoaderView("testBadRgbValue");
    List<ViewGameObjectRecord> collidableRecords = loaderView.getViewCollidableInfo();
    for (ViewGameObjectRecord record : collidableRecords) {
      if (record.id() == 5) {
        assertEquals(0, record.color().get(0));
      }
    }
  }
}
