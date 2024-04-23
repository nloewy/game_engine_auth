package oogasalad.model.gameengine.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import java.util.List;
import java.util.Map;
import oogasalad.model.gameengine.gameobject.GameObjectContainer;
import org.junit.jupiter.api.Test;
import oogasalad.model.gameengine.GameEngine;
import oogasalad.model.gameengine.command.MultiplySpeedCommand;
import oogasalad.model.gameengine.gameobject.GameObject;

public class MultiplySpeedCommandTest {

  @Test
  public void testMultiplySpeedCommand() {
    GameObjectContainer goc = mock(GameObjectContainer.class);
    GameObject gameObject = mock(GameObject.class);
    GameEngine gameEngine = mock(GameEngine.class);
    gameObject.applyInitialVelocity(2,0);
    when(gameEngine.getGameObjectContainer()).thenReturn(goc);
    MultiplySpeedCommand command = new MultiplySpeedCommand(List.of(1,2), Map.of(1, gameObject));
    command.execute(gameEngine);
    verify(gameObject).multiplySpeed(2.0);
  }
}