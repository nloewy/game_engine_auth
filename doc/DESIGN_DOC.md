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
    * The design emphasizes abstracting core game functionalities (such as the game engine) into
      reusable components or interfaces.
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
* Class #1: AuthoringFactory (DefaultAuthoringFactory), ShapeProxy, AuthoringProxy, Panel - Judy He
    * The DefaultAuthoringFactory implements the AuthoringFactory interface to enable configuration
      of Game Objects in the authoring environment of the game. The 4 methods implemented are
      createGameObjectsConfiguration(), createSurfacesConfiguration(),
      createCollidablesConfiguration(), and createPlayersConfiguration(). Each of these 4 methods
      are required for configuring a new Game Object. Hence, the AuthoringFactory is an abstraction
      that offers flexibility and extensibility if one wants to create different implementations for
      building the Game Object configuration part of the authoring enviornment.
      DefaultAuthoringFactory is then a concrete class offer one possible implementation.
    * The Panel class is an interface, an abstraction that allows flexibility and extensibility of
      the authoring environment. Implementations of the Panel interface include ShapePanel,
      PolicyPanel, ImagePanel, ColorPanel, InteractionPanel, KeySelectionPanel, and ModPanel. By
      creating combinations of Panels using the Container class, one can easily set up a new part of
      the authoring environment.
    * The ShapeProxy and AuthoringProxy in the authoring environment allow for sharing of common
      pointers across Panel classes that are responsible for keeping track of the current selected
      Game Object(s), the current configured attributes of a selected Game Object, all Game Object
      interactions, policies, key selections and mods during the authoring process. The final state
      of these data will be passed to the Game Builder through the AuthoringController to create a
      new JSON file for the newly authored game.

* Class #2: 

* Class #3: GameLoader (the parser)
    * The GameLoader (implemented by loader view and loader model) reads the JSON game data files into game objects and rules for the game engine and the frontend for gameplay; it uses reflection with annotation on the commands and conditions, etc. to instantiate and validate the commands and their respective parameters (they vary between different commands)

* Class #4: Command, Condition
  * The command class is the major part of the command pattern used in the game engine which handles the events triggered by two game objects colliding and in turn handles the flow of the game. Commands implement the command interface and are specified during authoring (using the interaction and policy panels mentioned above) and instantiated during parsing (in the parser class) both through reflection and annotation on the command classes and packages.


## Assumptions or Simplifications

* Decision #1: We have assumed that the user has a general understanding of how
  to build a game. We decided to give the game author a lot of creative freedom to encourage
  extensibility and versatility. However, the tradeoff here is that the user can create games that
  may be illogical or frustrating to play. For example, a user can create a game where no points can
  ever be scored. While this is certainly a legal decision to make, it does not create a very
  entertaining game.

* Decision #2: We decided that all game objects would be either circles/ellipses or
  squares/rectangles. This simplified our authoring environment as we only had to offer these two
  shape templates to the users. It also avoided physics bugs that may have arose from objects with
  unique boundaries colliding.

* Decision #3: We decided not to account for rotational dynamics to simplify the game engine. For
  example, in pool the cue ball can have different spins that affect its movement and collisions
  with
  other objects. In our game, we do not account for spin and assume that the ball is not rotating.

* Decision #4: We decided to assign all surfaces an infinite mass. This created the correct visual
  representation of a ball moving over a surface with friction.

## Changes from the Original Plan

* Change #1

* Change #2

* Change #3

* Change #4

## How to Add New Features

#### Features Designed to be Easy to Add

* Feature #1: Language Translations  
  Adding a new language involves creating a new properties file with the translations and creating
  an enum to represent the language.

* Feature #2: New Scenes or scene elements
  Scene elements are created by parsing xml files for parameters. If new styling is required, this
  can be done by adding a tag to a css file. If new event handling is required, an enum should be
  created. Adding the event simply requires writing the handler and mapping the enum to the handler
  in the appropriate handling class.

* Feature #3:

* Feature #4:

#### Features Not Yet Done

* Feature #1: Allowing user to specify surface incline in authoring environment  
  The current authoring environment does not allow users to select the incline of their chosen
  surfaces. There is backend support for surfaces of various angles, which is evident by the
  successful implementation of games like breakout and flappy bird that appear to have downwards
  gravity in relation to the screen.

* Feature #2: Leaderboards with varying stats  
  Currently, the leaderboards displayed show the top scores for the game that was last completed.
  This could be extended to show top scores within friend circles or most frequently played games.

* Feature #3: Consistent UI theme  
  Our game play environment has a consistent UI theme with functionality for the user to select
  different themes(currently default, dark, and fun). However, this has not been extended to our
  authoring environment which currently does not have any css styling.

* Feature #4: Live multiplayer games
  We would like to be able to have users play games on separate computers in real time. This would
  allow us to implement games like airhockey that require both players to have access to mouse
  controls at the same time.
 