package oogasalad.model.api;

import java.util.List;
import oogasalad.model.api.data.GameData;

/**
 * @author Alisha Zhang, Judy He
 */

public interface GameBuilder {

  <T> void buildGameField(GameData gameData, List<T> gameField);

}
