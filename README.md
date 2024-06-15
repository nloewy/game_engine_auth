# oogasalad

## TEAM 01 Fysics Fun

## Alisha Zhang, Doga Ozmen, Jordan Haytaian, Kevin Deng, Konur Nordberg, Noah Loewy

This project implements an authoring environment and player for multiple related games.

### Running the Program

* Main class: src/main/java/oogasalad/Main.java

* Data files needed: Game JSON files in data/playable_games, associated image files in
  data/background_images, data/nonstrikeable_images, data/strikeable_images, scene element xml files
  in data/scene_elements, language properties in main/resources/view/properties, default authoring
  environment values in main/resources/view/properties, exception messages in
  main/resources/model/error

* Interesting data files: css theme styling files in main/resources/view/styles

* Key/Mouse inputs: Buttons and game selection is done via mouse input (clicking), striking input
  default keys are up/down left/right (but each game has user specified input), controllable input
  is default a/d for left/right, and k for up (but each game has user specified input)

### Notes/Assumptions

* Assumptions or Simplifications: We have assumed that the user has a general understanding of how
  to build a game. We decided to give the game author a lot of creative freedom to encourage
  extensibility and versatility. However, the tradeoff here is that the user can create games that
  may be illogical or frustrating to play. For example, a user can create a game where no points can
  ever be scored. While this is certainly a legal decision to make, it does not create a very
  entertaining game.

* Known Bugs: Strikeable/collidable border behavior (strikeables can occasionally enter other
  collidables and become stuck inside), key listening/ root focus (during gameplay, focus will
  sometimes be taken away from the root node of the scene removing the users ability to strike),
  authoring environment shape rotation (parser does not save user specified rotation for game
  objects), error handling for improperly formatted images (JSON files with image paths that produce
  an error crash the program, they should ideally default to the specified color)

* Features implemented: Playing a variety of 2D physics games, authoring custom 2D physics games,
  language selection, theme selection, user profiles, game permissions, game mods, leaderboards

* Features unimplemented: Editing existing games in authoring environment

* Noteworthy Features: User profiles, friend circles, game permissions, game scores are stored in
  database, games have different mod options


