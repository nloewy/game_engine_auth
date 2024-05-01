package oogasalad;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import oogasalad.model.database.Database;
import oogasalad.view.scene_management.GameWindow;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws SQLException {
    Database d = new Database();
    d.registerUser("presidentPrice", "duke", "");
    d.registerUser("theNotoriousRcd", "design", "");
    d.registerGame("NotWorkingGame", "theNotoriousRcd", 1, "public");
    new GameWindow(primaryStage);
  }
}