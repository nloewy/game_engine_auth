# Game Design Project


## Noah Loewy, Alisha Zhang, Doga Ozmen, Jordan Haytaian, Kevin Deng, Konur Nordberg

This project implements an authoring environment and player for multiple related games. Before reading the code, please take a minute to peruse through [my reflection](https://github.com/nloewy/oogasalad_team01/blob/main/NOAH_DESIGN_REFLECTION.md). 
This project, originally for Duke's [CS308 - Advanced Software Design and Implementation](https://courses.cs.duke.edu/spring24/compsci308/) took more than 2 months to complete. I spent approximately 40+ hours/week working on the project.   

### Important Documents

1. [Link to Team's Design Document](https://github.com/nloewy/oogasalad_team01/blob/main/doc/DESIGN_DOC.md)
2. [Link to Noah's Personal Design Reflection](https://github.com/nloewy/oogasalad_team01/blob/main/NOAH_DESIGN_REFLECTION.md) 
3. Link to Noah's Primary Contributions to Codebase
   a. [Game Engine](https://github.com/nloewy/oogasalad_team01/tree/main/src/main/java/oogasalad/model/gameengine)
   b. [Game Engine Tests](https://github.com/nloewy/oogasalad_team01/tree/main/src/test/java/oogasalad/model/gameengine)

### Contributors
* Noah Loewy (me)
    * Game Engine
    * Game Engine Tests
    * Database
    * Various front end, parser bug fixes and refactoring
    * Angry Birds, Flappy Birds, Golf, Shuffleboard, Pool

* Doga Ozmen
    * Frontend Game Environment 
    * Authoring Environment (pre-refactor)
    * View Database Implementation
    * View Tests

* Jordan Haytaian
    * Frontend Game Environment
    * Authoring Environment (pre-refactor)
    * Bug Fixes
    * Game Environment Refactor

* Judy He
    * Game Parser (JSON to Game)
    * Game Builder (Authoring to JSON)
    * Authoring Environment (version supporting most updated Game Engine)
    * Authoring Environment (refactor - ex: AuthoringScreen, AuthoringFactory, UIElementFactory,
      Panel, Container, ShapeProxy, AuthoringProxy)
    * Bug fixes (authoring environment, Game Parser, Game Builder)
    * Tests for Game Parser, Game Builder

* Kevin Deng
    * Transformable node

* Konur Nordberg
    * Tests
    * Physics Engine
    * Database (SQL)
 
* Alisha Zhang
    * Parser (JSON to Game)
    * Builder (Authoring to JSON)
    * Load and save mid-game
    * Authoring environment (post-refactor): policy selection, interaction selection, keys selection
      panels
    * MOD (authoring part)
    * Authoring Environment game object panel (post-refactor): major bug fixes


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


