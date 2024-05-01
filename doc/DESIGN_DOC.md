# DESIGN Document for Fysics Fun

### Team: 01

### Names: Alisha Zhang, Doga Ozmen, Jordan Haytaian, Judy He, Kevin Deng, Konur Nordberg, Noah Loewy

## Team Roles and Responsibilities

* Team Member #1: Alisha Zhang
    * Parser (JSON to Game)
    * Builder (Authoring to JSON)
    * Load and save mid-game
    * Authoring environment (post-refactor): policy selection, interaction selection, keys selection
      panels
    * MOD (authoring part)
    * Authoring Environment game object panel (post-refactor): major bug fixes

* Team Member #2: Doga Ozmen
    * Frontend Game Environment 
    * Authoring Environment (pre-refactor)
    * View Database Implementation
    * View Tests

* Team Member #3: Jordan Haytaian
    * Frontend Game Environment
    * Authoring Environment (pre-refactor)
    * Bug Fixes
    * Game Environment Refactor

* Team Member #4: Judy He
    * Game Parser (JSON to Game)
    * Game Builder (Authoring to JSON)
    * Authoring Environment (version supporting most updated Game Engine)
    * Authoring Environment (refactor - ex: AuthoringScreen, AuthoringFactory, UIElementFactory,
      Panel, Container, ShapeProxy, AuthoringProxy)
    * Bug fixes (authoring environment, Game Parser, Game Builder)
    * Tests for Game Parser, Game Builder

* Team Member #5: Kevin Deng
    * Transformable node

* Team Member #6: Konur Nordberg
    * Tests
    * Physics Engine
    * Database (SQL)

* Team Member #7: Noah Loewy
    * Backend Database Integration
    * Game Engine
    * Model Tests
    * Frontend Bug Fixes

## Design Goals

* Goal #1: Modularity and Extensibility
    * The architecture was designed to be very modular, allowing for the easy addition of new
      components and features without breaking existing functionality.
    * This modularity makes sure that each module can be developed, tested, and maintained
      independently, increasing flexibility and extensibility.
    * Interfaces and abstract classes were implemented to define clear boundaries between modules
      which allowed for easy substitution/extension of components. For example, the
      *ExternalGameEngine* interface provided a contract for interaction between the game controller
      and the game engine,
      enabling alternative implementations to be integrated seamlessly.

* Goal #2: Configurability
    * The system was designed to be highly configurable, enabling users to customize various aspects
      into their own games that they can create in the authoring environment without changing the
      codebase or writing a line of code.
    * By abstracting configuration parameters, the system promotes adaptability to diverse game
      requirements.

* Goal #3: Abstraction of Core Functionality
  * The design emphasizes abstracting core game functionalities (such as the game engine) into reusable components or interfaces. 
  * This abstraction layer separates the high-level game logic from the implementation details.


#### How were Specific Features Made Easy to Add

* Feature #1: New Commands
  * Make a class that implements Command
  * Give annotation with expected parameter number
  * You've just made a new command!

* Feature #2: New UI Elements

* Feature #3: New Turn Policies
  * Make a class that implements Turn Policies
  * You've just made a new turn policy!

## High-level Design

#### Core Classes and Abstractions, their Responsibilities and Collaborators
* Class #1: AuthoringFactory (DefaultAuthoringFactory) - Judy He
    * The DefaultAuthoringFactory implements the AuthoringFactory interface to enable configuration
      of Game Objects in the authoring environment of the game. The 4 methods implemented are
      createGameObjectsConfiguration(), createSurfacesConfiguration(),
      createCollidablesConfiguration(), and createPlayersConfiguration(). Each of these 4 methods
      are required for configuring a new Game Object. Hence, the AuthoringFactory is an abstraction
      that offers flexibility and extensibility if one wants to create different implementations for
      building the Game Object configuration part of the authoring enviornment.
      DefaultAuthoringFactory is then a concrete class offer one possible implementation.

* Class #2: Panel - Judy He, Alisha Zhang
    * The Panel class is an interface, an abstraction that allows flexibility and extensibility of
      the authoring environment. Implementations of the Panel interface include ShapePanel,
      PolicyPanel, ImagePanel, ColorPanel, InteractionPanel, KeySelectionPanel, and ModPanel. By
      creating combinations of Panels using the Container class, one can easily set up a new part of
      the authoring environment.

* Class #3: ShapeProxy, AuthoringProxy - Judy He
    * The ShapeProxy and AuthoringProxy in the authoring environment allow for sharing of common
      pointers across Panel classes that are responsible for keeping track of the current selected
      Game Object(s), the current configured attributes of a selected Game Object, all Game Object
      interactions, policies, key selections and mods during the authoring process. The final state
      of these data will be passed to the Game Builder through the AuthoringController to create a
      new JSON file for the newly authored game.

* Class #4:

## Assumptions or Simplifications

* Decision #1

* Decision #2

* Decision #3

* Decision #4

## Changes from the Original Plan

* Change #1: No Live Gameplay
  * We decided to not implement live gameplay because it ended up being on a lower priority list.
  * We implemented database with permissions instead.

* Change #2: Decided to do MODS
  * We changed our plan to have mods instead of a social center.

* Change #3: Collision Handling from Front to Backend
  * Originally we had planned for collision handling to be implemented in the frontend. 
  * We Had inserted collision handling to be a method in the ExternalGameEngine API in order to handle it in the frontend and pass it to the backend.
  * We then moved collision handling to be handling in the internal game engine.

* Change #4

## How to Add New Features

#### Features Designed to be Easy to Add

* Feature #1: Language Translations
  * Adding a new language involves creating a new properties file with the translations and creating
    an enum to represent the language.

* Feature #2: New Scenes or Scene Elements:
  * Scene elements are created by parsing xml files for parameters. If new styling is required, this
    can be done by adding a tag to a css file. If new event handling is required, an enum should be
    created. Adding the event simply requires writing the handler and mapping the enum to the handler
    in the appropriate handling class.

* Feature #3: Themes
  * Themes are created using a css file for the specific theme in combination with an enum.
  * Therefore, if a developer/user were to want to add a new theme, all they would need to do is to create a new css file for the theme, and to add the theme onto the UITheme enum.

* Feature #4: A New Database Field
  * If a user/developer wanted to add a new field onto the database, such as favorites (favorite games), it would be a very simple change into the database.
  * The only requirement would be to add a new table onto the existing database called for example favorite games which would be connected to the users, and the existing games.

#### Features Not Yet Done

* Feature #1: Allowing user to specify surface incline in authoring environment
  * The current authoring environment does not allow users to select the incline of their chosen
    surfaces. There is backend support for surfaces of various angles, which is evident by the
    successful implementation of games like breakout and flappy bird that appear to have downwards
    gravity in relation to the screen.

* Feature #2: Leaderboards with varying stats
  * Currently, the leaderboards displayed show the top scores for the game that was last completed.
    This could be extended to show top scores within friend circles or most frequently played games.

* Feature #3: Consistent UI theme
  * Our game play environment has a consistent UI theme with functionality for the user to select
    different themes(currently default, dark, and fun). However, this has not been extended to our
    authoring environment which currently does not have any css styling.

* Feature #4: Live multiplayer games 
  * We would like to be able to have users play games on separate computers in real time. This would
    allow us to implement games like airhockey that require both players to have access to mouse
    controls at the same time.
 