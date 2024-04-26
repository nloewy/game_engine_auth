# OOGASalad Stand Up and Retrospective Discussion

### TEAM 01

## Stand Up Meeting

### Noah

* Work done this Sprint
  This week I spent a great deal of time working on refactoring the GameEngine and adapting it to
  allow for the creation of new games. I developed both shuffleboarding and pinball, wrote the
  necessary commands for it, as well as wrote integration tests for them. I also spent time
  documenting and writing unit tests for the game engine. Additionally, I improved the design of the
  game engine by adding an adaptor pattern with the concepts of scoreables, controllables, and
  strikeables.

* Plan for next Sprint?

I hope to have time to start working on the backend aspects of the Database and both extension
requirements, as well as live games such as soccer and air hockey. This will require some
interaction with the GameController as well. Furthermore, it will require the use of a socket to
allow players to use multiple devices. The GameEngine is mostly done, but I will likely need to make
slight changes based upon what the authooring enviornment is able to do.

* Blockers/Issues in your way

One major blocker that I am facing is the unfinished authoring enviornment, as it makes it take
significantly more time to make test json files for integration tests. A lot of it needs to be
hardcoded for now, and the hardcoding needs to change when certain changes are made to the backend,
which complicates things. The best thing I can do is to be patient and offer to help with the
authoring environment when needed.

### Kevin

* Work done this Sprint

* Plan for next Sprint?

* Blockers/Issues in your way

### Doga

* Work done this Sprint
    * Worked on planning/helping with the refactoring of the authoring environment. Specifically,
      the splitting of the
      AuthoringScreen to different functionality and UI component based classes.

* Plan for next Sprint?
    * Continue refactoring; implement transition and pause screens; make more view tests; improve
      the look of the game (visuals/graphicsg)

* Blockers/Issues in your way
    * The refactoring is very extensive and time-consuming. In addition, I can't begin writing more
      view authoring environment tests until the refactoring is finished.

### Judy

* Work done this Sprint
  * For this sprint, I worked on refactoring the authoring environment. I made numerous abstractions including Panel and Container, and implemented the Proxy pattern (ShapeProxy, AuthoringProxy) for enabling a common pointer to the current selected shape and keeping track of creation of items like GameObject, Collidable, and Surface. This eliminated the need for multiple authoring screen classes. There is only one class, AuthoringScreen, now for displaying the authoring environment. Rather than generating a new window for each page, the authoring environment now includes a dropdown listing the different possible pages. When a page is selected, the screen re-populates itself, particularly the Container section. This allows for more flexibility as the user can now go back and forth between different configuration pages.   

* Plan for next Sprint?
  * Debug drag/drop shape from shape templates.
  * Using the AuthoringProxy to successfully save all configured data from the authoring environment.
  * Refactor Panel classes.
  * Refactor AuthoringScreen class, specifically the part where permanent UI elements are initializes (finishButton, page dropdown, canvas).
  * Remove hard-coding in all parts of the authoring environment.
  * Move the order of different combination of Panel for each page to a data file. 

* Blockers/Issues in your way
  * Determining the best way to refactor the Panel classes.
  * Debugging the drag/drop of shapes from shape templates.
  * Sourcing the set-up of pages of the authoring environment from data files.   

### Alisha

* Work done this Sprint
  * Added exception handling for commands through annotation
  * Updated parser and builder according to changes in model
  * Added new annotations mostly for commands and rule types
  * Made new policies panel for the authoring environment 

* Plan for next Sprint?
  * Finish the interactions and policies authoring part as soon as possible
  * Update record creation according to authoring environment changes
    * Maybe separate out rules and objects/layout into separate JSON files + allow users to upload existing rules when authoring new game
  * Make the player login/profile frontend (maybe also the leaderboard?)
  * Add error handling + warnings to the authoring environment + make logging work better

* Blockers/Issues in your way
  * Finding a good way to pass all data from authoring environment into the records

### Jordan

* Work done this Sprint
    * This sprint I worked on refactoring the scenes on the game player side of the program (menu,
      title, pause, transition, game). They are now in the process of being created via a factory
      which parses from xml and css files instead of hardcoding. I also worked on refactoring the
      authoring environment. I worked on changing the properties/attributes of game objects and
      changing the game object selection screen to prompt to the correct attributes.

* Plan for next Sprint?
    * Next sprint I would like to continue my work with the authoring environment. Next steps for
      the game object aspect are transferring the attribute info to a map for JSON creation and
      ensuring that users cannot submit until they have selected all necessary attributes. I would
      also like to continue my work in refactoring the game play scenes. Lastly, I would like to
      start on the front end implementation of mouse controllable objects.

* Blockers/Issues in your way
    * Complying by new authoring environment design, ensuring best way to communicate/store game
      object attributes for JSON creation

### Konur

* Work done this Sprint

* Plan for next Sprint?

* Blockers/Issues in your way

## Project's current progress

Authoring Enviornment design being significantly improved every day. The game engine is nearly done.
The extensions are not being worked on, so that needs to start ASAP. It is tricky because this is a
week where everyone has midterms, but we are still chugging along and making a ton of process. We
are also working towards making the front end and authoring enviornment a lot more configurable, to
remove hardcoding whenever possible.

## Current level of communication

The communication has been great. We are using our group chat frequently and meeting almost nightly
to work together, have a stand up meeting, and go over necessary tasks. This has been extremely
useful in making sure everyone is on task and has something to work on. We can probably do a better
job of explaining what we are currently working on at all times and how it relates to our goals for
each sprint.

## Satisfaction with team roles

I think we are mostly satisfied with team roles. Every few days they are definitely shifting as
people have midterms, but we are all trying to share the burden of the work. I think we need to put
a larger emphasis on the higher priority things in the backlog. I think there are some
refactorings / features that ought to be emphasized more for the sake of demos / getting a working
project.

## Teamwork that worked well

* Thing #1: We should continue to have daily stand-ups and work together every night using peer
  programming, and also just bouncing ideas off of each other
* Thing #2: We should continue to articulate our availability, plans, and expectations for the week
  so that everyone is on the same page with respect to deadlines.

## Teamwork that could be improved

* Thing #1: We should try to do a better job of communicating what branches we are currently
  updating, and what features we are working on, to avoid merge conflicts.

* Thing #2: We should try to do a better job of practicing demos and presentations as a whole team,
  and make it clear who is going to present what, and what exactly they should focus on

## Teamwork to improve next Sprint

Next sprint, I think we should put a larger emphasis on the backlog and adding weights to certain
tasks. We are approaching crunchtime, and we ought to make sure that we are doing everything we can
to make the best-designed, feature-packed project by the deadline. Focusing on less-important /
less-critical design choices will only be a time-suck and not help us achieve this goal.
