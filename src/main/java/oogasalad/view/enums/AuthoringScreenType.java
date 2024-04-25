package oogasalad.view.enums;

public enum AuthoringScreenType {
  GAMEOBJECTS("Game Objects"),
  INTERACTIONS("Interactions"),
  POLICIES("POLICIES");

  private final String string;

  AuthoringScreenType(String name){string = name;}

  @Override
  public String toString() {
    return string;
  }
}
