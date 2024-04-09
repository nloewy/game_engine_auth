package oogasalad.model.gamebuilder;

import java.util.List;
import oogasalad.model.api.data.CollidableObject;
import oogasalad.model.api.data.GameData;
import oogasalad.model.api.data.Variables;

public class VariablesBuilder implements GameBuilder{

  @Override
  public <T> void buildGameField(GameData gameData, List<T> gameField) {
    List<Variables> vars = (List<Variables>) gameField;
    gameData.setVariables(vars);

  }
}
