package oogasalad.view;

import java.util.List;
import javafx.scene.Scene;
import oogasalad.model.api.CollidableRecord;
import oogasalad.model.api.GameRecord;
import oogasalad.view.Screen.GameScreen;
import oogasalad.view.Screen.MenuScreen;
import oogasalad.view.Screen.TitleScreen;
import oogasalad.view.VisualElements.CompositeElement;

public class SceneManager {
  private CompositeElement compositeElement;
  private GameScreen gameScreen;


  public SceneManager() {
    this.compositeElement = new CompositeElement();
  }

  public void update(GameRecord gameRecord) {
    compositeElement.update(gameRecord.collidables());
  }

  public boolean notMoving(GameRecord gameRecord){
    return gameRecord.staticState(); //will be added to record eventually
  }

  public Scene makeTitleScreen(Controller controller){
      TitleScreen titleScreen = new TitleScreen(controller);
      return titleScreen.getScene();
  }
  public Scene makeMenuScreen(List<String> titles, Controller controller){
    MenuScreen menuScreen = new MenuScreen(titles, controller);
    return menuScreen.getScene();
  }
  public Scene makeGameScreen(Controller controller, CompositeElement compositeElement){
      gameScreen = new GameScreen(controller, compositeElement);
      return gameScreen.getScene();
  }

  public void enableHitting(){
    gameScreen.enableHitting();
  }


  public void makeTransitionScreen(){
//    TransitionScreen transitionScreen = new TransitionScreen();
//    return TransitionScreen.getScene();
  }
}
