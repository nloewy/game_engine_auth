# Journal : Game Authoring/Engine Project Reflection
#### Noah Loewy


## Design Review

### Description of the Overall Design

On a higher level, there were 5 modules that communicated amongst each other that allowed for
our program to run. The `GameView` screen represents the panels that highlight the active
gameplay. The `Database` allows for users to save their profiles and statistics online. The
`AuthoringEnviornment` allows for the creation of games and their respective JSON files. The
`GameParser` allows for the aforementions JSON files to be parsed into `GameObjects` and
`Player` objects. Lastly, the `GameEngine` contains all of the mechanisms for actually playing
the game, including the physics engine, turn manager, score manager, and collidable object
manager. The view components interact with both the model and each other using a series of
Controllers. The model component utilizes annotation and reflection to promote flexiblity when
adding new features to the authoring environment, and employs the Command Pattern, Adapter
Pattern, Factory Pattern, and Chain of Responsibility Pattern internally.

## Reflecting on *MY* Code [I served as primary contributor]

### Assumption or choice that had a global or significant impact on your design

One major assumption that I made over the course of the design-process that had a large impact 
on the rest of my group was the organization of the `GameObject` abstraction hierarchy. We 
wanted to be able to represent every object in the Game Space the same way, as it would make the 
authoring process significantly easier, and also allow for more flexibility during game play. 
This included determining how we would handle collidable objects versus surfaces, as well as 
objects with special properties such as strikeables. In order to make this decision, I had to 
commit to the assumption that the physics of a collision can be determined during the 
`GameLoading` phase, which I was comfortable doing, as all variations of the laws of physics 
that we were considering adhered to this principle. Thus, the use of a singular GameObject class 
made it significantly easier for my teammates, as only one generic type of object needed to be 
constructed during building, uploaded during parsing, and updated during the game.

Design Challenges

#### Design #1

* Trade-offs

One major design challenge that I grappled with over the course of this project referred to how
the state of the game could change "on the fly". After walking through hundreds (no joke) of use
cases, I was able to separate these types of events into three different categories. The game
objects could move as a result of the laws of physics, meaning that objects can bounce off each
other, roll on a surface, etc. Furthermore, certain collisions could trigger this change in game
state, for example the intersection of a golf ball and hole. Lastly, reaching a "static state",
which signified the end of the turn (and served as its own abstraction, as the definition of
"static" switched from game to game), could also trigger its own consequences, such as the
change in which player is currently active. The big question I faced was determining a mechanism 
to handle these three types of "event-causers" uniformly, while allowing opportunities for new 
types of "causers" and events to be easily added.

* Alternate designs

There were numerous alternatives discussed when designing the collision handling, and overall I 
am very pleased with the route I chose to go. At first, we considered doing collision detection 
and handling in the front end (from a physics perspective), and then handling the 
"game-specific-events" immediately following. Once a static state was reached, then those 
specified events would occur. We also considered utilizing the visitor and mediator patterns, with 
some sort of intermediary interface managing the collisions and informing collision handler 
objects based on who was responsible for handling it.

* Solution

In the end, I chose to center our approach around an abstracted physics engine and a Command 
pattern. During each time step, the physics handler first deals with the actual physics of the 
collision. It is important to note that the physics handler is completely abstracted away from 
the `GameEngine` (I didn't even write/design it), and it enables for the decoupling of the game 
state and the actual physics logic. Furthermore, both collisions and certain static states 
(where different types of static states can be differentiated through abstract `Conditions`) 
were both handled through the use of a `Command` pattern. In the authoring environment, game 
creators could assign certain events to happen when two objects collided, or when certain game 
conditions were met. The Command pattern was able to apply to both scenarios, even though they 
were extremely different from each other, highlighting its flexibility.

* Satisfaction (Justification or Suggestion)

The Command pattern encapsulates the actions that modify the Game State as objects, allowing for 
the parameterization of behaviors, while also decoupling the cause of the action from the action 
itself, which the alternatives we considered failed to do. This allows for new events, commands, 
conditions, and physics engines to be created without modifying any of the existing code, which 
is aligned with the open-closed principle.  

#### Design #2

Handling the fact that some game objects had properties that other didnt

* Trade-offs

One design issue that I encountered, which was a major focus of both sprints 1 and sprint 2 for 
me, was the fact that some game objects had additional properties on top of its typical game 
object properties. Furthermore, it was important for client code to be able to access only those 
game objects, and distinguish between types. Of course, this all needed to be done without 
direct downcasting or `instanceof` checks, which made it significantly harder. This included 
adding functionalities for `scoreable` objects (which earn points), `strikeable` objects (which 
can be hit), and `controllable` objects, which the user can control using its keyboard and/or 
mouse.  One of the major tradeoffs that presented itself was that of type safety versus 
flexibility, as I aimed to find a solution that allowed for flexible code that would still 
adhere to the Liskov Substitution Principle, and not require any downcasting checks. Furthermore,
I needed to examine the trade-off between composition and inheritance. Whereas a composition 
based approach would allow for complicated objects to easily be created by combining simpler 
objects, it may lead to a very large number of interfaces, especially if the codebase were to 
expand.

* Alternate designs

As mentioned above, the two designs that I considered included a composition and inheritance 
based approach. In the inheritance based approach, all of the "add-on" properties that game objects 
can inherit existed as interfaces that game objects could implement. However, this was 
problematic because the `GameEngine`, which had a list of `GameObjects`, would not be able to 
directly know if the object it is referring to holds additional properties, or if it is a normal 
game object.

* Solution

In the end, I decided to use a composition based approach, specifically one that was inspired by 
a combination of the Adapter Pattern and Strategy Pattern. Essentially, there were additional 
interfaces (or "components") that game objects could have as an instance variable. Those 
components also held references to the game object, and by calling methods on the component, if 
they existed (which java optional handled), I was able to access only certain types of game 
objects. 

* Satisfaction (Justification or Suggestion)

This approach offered modularity and encapsulation by allowing behaviors to be encapsulated within 
separate properties/components, making reusing code significantly easier code. Furthermore, it 
creates a mechanism for game objects to adapt during runtime and attach/detach certain 
components and properties. Although there are no examples of this in our games, the framework is 
completely there, and this can be done very easily. Lastly, code that calls the `GameObject` 
does not need to be aware of all the "properties" it can possess, as that encapsulation logic is 
hidden away in protected classes.

### Design Pattern

Command Pattern

* Design problem applied to

One of the major design problems that I encountered involved developing some sort of abstraction 
that can handle and trigger "events" in the backend. However, these events came in all 
different shapes and sizes, required differing numbers of parameters, and could be 
called in a variety of different concepts. Furthermore, some sort of representation of Commands 
needed to exist in the authoring environment, so that it would be possible to assign commands to 
certain events.

* Classes and methods that implement it

The Command pattern is primarily utilized via the Command interface, and all of its inheritors 
in the command package. Furthermore, it is used by the Condition interface and all of its 
inheritors, although in a slightly different manner. Both are examples of delayed execution of 
actions.

* How it helped your design

The main reason the Command pattern facilitated my design is that it made my code considerably  
more extendable. Adding new types of actions that could occur in the `GameEngine` was 
significantly easier with this abstraction, as changes simply required creating a new subclass. 
Furthermore, it separated the creation and definition of these actions in the authoring 
environment, from the actual execution of them in the Game Engine. Lastly, and perhaps most 
importantly, by being able to represent Commands, Conditions, and other types of actions as 
objects, I am able to trigger them in a variety of different ways, which removes duplicated code.
For example, the same command can be called during a static state or as a result of a collision, 
and the command would not be able to tell the difference. 


### API Review

* Describe as Service

When viewing the `ExternalGameEngine` API as a service, it's role in the overall scope of the  
program is clear: it is a mechanism of connecting the backend game-engine with the view side and 
its controllers, and it allows for essential game operations, such as starting a level, updating 
a timestep, "undo-ing" a timestep (or multiple timesteps and going back to a static state), or 
handling user prompts (of course this is called by a controller).

* Easy to use and hard to misuse

There are a few factors that contribute to ease of use for the `ExternalGameEngine` API. For 
starters, the entire class is well-documented using Javadocs, and all the methods names are 
descriptive and concise. Furthermore, the API only focuses on essential and high level game 
operations (updating, starting, stopping), as opposed to game specific details (handling 
collisions, determining the winner, etc). This allows for the game developers to be able to 
focus on the specific functionality, while still having a broad and more generalized API to work 
with. Additionally, avenues for misuse is mitigated because of the fact that update returns 
immutable records, preventing clients from modifying backend data. Furthermore, parameter 
constraints, particularly when dealing with controllables, ensure robust error handling. 

* Encapsulation and extension

The `ExternalGameEngine` API does a really strong job of encapsulating game specific logic and 
commands away from the front end. All of the methods in the API return immutable records or 
primitives, which prevent the front end from being able to access back end data. Furthermore, 
all the publicly defined methods in the API are relatively broad, so the actual implementation  
details can be kept within private helper methods or other classes within the gameengine module. 
Overall, as a result of the relatively broad methods, the underlying implementations and private 
helper methods are shielded away. Furthermore, the usage of an interface to represent the API 
will allow for alternative game engines to be used, perhaps that don't rely on a command based 
system, without having to change the code for the game view at all.  

### Feature Design Reviews

#### Good Example **you** implemented


* Design

One feature that I implemented where I am particularly proud of the feature's design is the rank 
abstraction. This feature was not only extremely enjoyable to implement, as it brought me back 
to my CS 201 glory days, but it also is a really good example of the power of abstraction. The 
rank policy abstraction refers to ordering the players from first to last place. This is useful 
in the context of our current framework for a few reasons: displaying the live score, adding 
winners and losers to the database, and dealing with games where the order of current placement 
can affect the turn-ordering or other aspects of the game engine. Essentially, the 
PlayerRecordComparator, the high-level abstract class, has a single public compare method, which 
determines the ordering of two player records. By default, this method will go based on the ids 
of the players. However, games can use custom comparators, in order to rank players from low to 
high in score, high to low in score, or even based on other factors (such as proximity to the 
hole in golf). The rank comparator is dependency injected into the game engine via the rules 
record, and is invoked with each update step.

* Evaluation

The Rank Comparator exemplifies good design principles because of its use of abstraction to 
encapsulate the ranking mechanism from client code. Allowing game designers to easily choose 
from a variety of comparison based methods gives them a ton of flexibility when designing games, 
instead of confining them to games where the winner is always the player with the most points. 
Furthermore, it opens up the door for opportunities for players to be able to define custom 
comparators in the future (this would require some SLogo-like parser, of course). As a very 
small functional interface, the rank feature is modular and easily testable, making it reusable 
and adaptable as game requirements evolve. Lastly, the use of dependency injection limits the 
coupling between the game engine implementation and the rank implementation, as it can now be 
game-specific, as defined in the authoring environment.

#### Needs Improvement Example **you** implemented

Static State Handler Abstraction

* Design

Similarly to the aforementioned Rank Comparator, the Static State Handler abstraction is 
injected into the `GameEngine`to facilitate loose coupling between the handler and the game 
engine itself. The Static State Handlers are designed using the Chain of Responsibility pattern. 
Essentially, the `GameEngine` is passed in a doubly-linked list of `StaticStateHandler` objects, 
which are each associated with a `Condition` object and a `Command` object. If the `Condition` 
is met, then the `Command` is executed, and we jump to the `previous` item in the linked list. 
If the `Condition` is not met, then we jump to the `next` item in the linked list. While the  
implementation described mentions a hardcoded order of handlers ("Game End", "Round End", "Turn  
End"), the design allows for flexibility and extensibility. By decoupling the handler 
construction  from the GameEngine, it's possible to dynamically configure the handler chain 
based on the requirements of different games. However, this would need to be added in a future 
refactoring session.

* Evaluation

This setup allows for flexible and dynamic handling of various states or conditions within the 
game without tightly coupling the handling logic with the GameEngine itself. Each 
StaticStateHandler is responsible for a specific condition, promoting separation of concerns and 
easier maintenance and extension of the codebase. However, although the usage of the Chain of 
Responsibility pattern is good, there exists some unnecessary hardcoding that could be removed. 
In the `GameLoader`, a the `StaticStateHandlerFactory` is called to create the linked list of 
handlers. However, the implementations of `StaticStateHandler` in the linked list, and their 
specific order, is hardcoded as "Game End", "Round End", "Turn End". Although this makes sense 
for all of the games we developed, there may be other more advanced games that have a more 
complicated structure. This limits the extensibility of this abstraction, as hardcoding specific 
implementations and their order restricts the flexibility to adapt to different game structures. 
If I had more time to refactor this code, I would aim for the types of handlers in the chain 
should be specified in the authoring environment, prior to determining the commands and 
conditions associated with each chain. Nonetheless, this was my first time implementing a Chain 
of Responsibility pattern, and I am generally proud of the way it turned out.

## Reflecting on *MY TEAMMATES* Code [I *DID NOT* serve as primary contributor]

### Open for Extension

* Adding new game variation

Creating a new game variation is quite easy. Games can be created in the authoring environment
by clicking "Create" on the main screen and following the prompted instructions. This will
automatically create a JSON file and add the game to the database. The game author is required
to enter in all the policies and collision handling for their new game. No code is required
to be edited for the creation of a game. The only potential necessary change to the repository
necessary would be adding new images to the data folder before using the authoring environment.
This adheres to the Open Closed principle, as extension is extremely easy, without requiring the
modification of any existing code.

* Changing data file format

Relatively speaking, changing the format of the game data files from JSON to an alternative
format, such as XML, would be somewhat easy. This is a product of our flexible design. In
order to make this change, you would need to update the ```parseJSON``` method of GameLoader to
expect a file with a different extension (ideally the file extension would be configurable, as
opposed to hardcoded as the static constant `JSON_EXTENSION`). Furthermore, the errors thrown in the
`GameLoader` constructor should be configured so that the error message is not reliant on the
usage of JSON. The usage of the Jackson `ObjectMapper` JSON parser would need to be replaced by
an alternative external API call that can handle the new format of file. The `GameBuilder` class
would mostly stay the same, except the Jackson JSON annotations would need to be removed or
replaced with the correct annotations. Overall, although the file format is not as flexible as
we would like it to be, this can easily be changed by continuing to apply the principles of
encapsulation. If we were to encapsulate the implementation details of the parser and builder
into their own abstractions, changing the format would just involve implementing the required
interface.

* Add new Player view

Updating a new player view, such as game metadata or statistics, would be relatively easy to do
as a result of our adherence to the Open-Closed principle. For starters, a new XML file would
need to be created representing the new scene, and all the specific objects in the view that would
be displayed must be enumerated in the JSON file. Furthermore, the `SceneManager` would need to
add a new method that switches to the new scene. If we are solely updating something within a
pre-existing scene, the change is even easier: simply update the XML file. However, this assumes
that a database method already exists for the specific query (e.g. getting the most played games)
, so adding to the `Database` API may be necessary. Overall, the encapsulation of our view
elements into XML makes adding new elements to the UI very easy, however our failure to create
abstractions within the Database module may make retrieving that information require new code.
Note that this may be because we were unable to start designing and implementing our database
until the tail end of the product, as the extensions were towards the bottom of our backlog.

* Extension Feature

One new extension feature that would be relatively easy for our team to add would be the online
saving of game data through a REST API or online database. We already have an online database
configured with a relatively strong `Database` API. There are different levels as to how this
can be done. One naive method would be to save the entire JSON as a column in the `Games` table
of the database, as Postgres supports JSON quite nicely. One benefit of this is that it would
require minimal changes to our existing code: only methods for adding, mutating, and retrieving the
JSON information and game data for a given game would be required. However, this would be
relatively challenging to abstract away for different filetypes, and also serve as a potential
violation of the Single Responsibility Principle, so it would be ideal to save each object  
(component) of the game individually. Fortunately, given the fact that the`GameBuilder` and  
`GameParser` examine each element individually, adding new calls to the Database would make this
quite manageable.

### Design Decisions

#### Significant Change

One significant change that our design underwent was the creation of Commands during the
authoring phase. Note that by "commands", I am not referring to the actual `Command` objects
used in the `GameEngine`, but rather the Map representation of the Commands used in the
authoring environment and the data files.

* Alternate design #1

Our original method of handling the creation of Commands was to have each command take in an
arbitrary list of arguments. When the actual `Command` objects were instantiated in the
`ExecutableFactory`, the length of the argument list would be irrelevant. Instead, the arguments
would not matter until runtime: when the command was actually executed. For example, the
`SetVisibleCommand` requires two inputs: a `GameObject` and a boolean (0/1). If only one parameter
was entered, an error would not be thrown until the game is actually played. Similarly, if three
or more parameters were entered, only the first two would be used, unbeknownst to the user.

* Alternate design #2

After careful thought, we realized it would be necessary for the authoring environment to be
aware of how many parameters are required for a given `Command`, and created a map from command
type to expected command number. We quickly realized that this would be a massive violation of
Open-Closed principle, and make adding new commands extremely difficult. We then decided to use
Java custom annotations, and gave each Command an annotation with the expected number of
parameters and a brief description. We did this through the creation of the
`ExpectedParamNumber` interface / annotation.

* Trade-offs

We carefully analyzed the tradeoffs of this design decision, before moving forward with a final
choice. On one hand, not restricting the number of possible parameters during Command creations
could allow for the possibility of overloaded commands, which could take varying numbers of
parameters. However, Java's annotations would allow us to allow for a range of possible values,
so this was a non-issue. Furthermore, design #1 would not require explicit declaration of
expected parameters, and although that may seem beneficial to users, it can result in confusing
runtime errors in the long-term. Design #2 adheres to the open-closed principle, as it can
easily be extended by adding new types of annotations (for example if a range is used), while
also providing an additional protection against runtime errors (assuming the JSON is not changed)
.  The primary downside to design #2 is the additional overhead and programming required for
implementing the custom annotations.

* Justify your Preferred Solution

After careful and extensive deliberation, our team decided to change course and use the second
option. Although custom annotations would provide us with a fair amount of additional work, we
believed that annotations would facilitate the addition of more complicated commands to the
`GameEngine`. Furthermore, it's robust error checking would make the user-experience much more
enjoyable, and also provide an opportunity for customized parameter descriptions for each
command.

* Satisfaction with Chosen Solution

Overall, I am satisfied with the chosen solution (Alternate Design #2) as it effectively
addresses the trade-offs discussed above. By leveraging custom annotations, we are able to
provide a much clearer picture of the Command's parameters, while still maintaining an
extensible and robust codebase. This serves as its own form of error handling, and takes some of
the burden off of the game designer to understand how all the Command's work. Furthermore, we
are still able to encapsulate the implementation of each Command, as only the annotation was
examined.


#### Implementation Change

One major design decision that went "under the radar" as a result of it being hidden behind
abstractions was the usage of `Panels` in the authoring environment. One of our major
implementation goals for this project was to give the game designer enough flexibility to be
able to make a wide variety of games, but also not overwhelm them with too many requirements.

* Alternate design #1

We originally had the entire authoring panel on one screen. The authoring environment had
sequential phases, so that users would first add the necessary game objects to the screen,
followed by the interaction, policies, and so on. Once necessary specifications were made in the
authoring environment, they would disappear so that the next "phase" of authoring could commence,
and a new scene was switched to for the next phase.

Alternate design #2

Instead of having multiple scenes, we also considered using one scene, with multiple panels. The
screen could be split up so that the left side (which contained all the game objects),
remained constant and present throughout the entire authoring phase, whereas the right side,
which contained options for features selections, would be dynamic. We called this abstraction a
`Panel`, and allowed users to toggle between panels using a drop-down on top of the screen.


* Trade-offs

Design #1 provides a more structured and simplistic approach to the authoring process for users,
breaking game creation into sequential "byte-sized" phases for users. However, although it may
seem simplistic, it can significantly limit flexibility as users switch between scenes for each
phase. On the other hand, Alternate Design #2 allows for the creation of an extendable
abstraction – Panel – and keeps the panels together in one scene, allowing for a simpler
transition between different phases of the authoring process. Most importantly, the second
option allows for a much more extendable design. The use of the panel abstraction allows for
panels to be added and removed very easily, and without any new code. Furthermore, it allows for
the panel options to be configured, instead of a hardcoded list of phases. Finally, users are able
to change the structure of an individual panel without affecting all the others, which
significantly reduces the amount of changes required for a modification.

* Justify your Preferred Solution

We strongly preferred the second solution, which employs the `Panel` abstraction and the use of
only one scene for authoring. Given that one of our major design goals was to allow for author
flexibility, we felt that being able to easily add, edit, and delete panels is a "must".
Consequently, we felt it was necessary to go the route involving XML configurations and reflection,
as it adheres to open-closed, enabling easier modifications without extensive code changes.

* Satisfaction with Chosen Solution

I am satisfied with our usage of the Panel abstraction, since it keeps all the authoring
features from Design #1, but also creates room for expansion. If we were to expand our game
engine to include new abstractions and new features, the authoring environment would need to
expand with it (assuming default values would not be used). Consequently, it is extremely
important to have an authoring environment that can easily be modified, and this design choice
allows for exactly that.


### API Reviews

#### Stable

* Describe as Service

The `AnimationManager` class is another frontend API that my teammates also worked on, which
manages the animations and timelines required for making the game flow. These animations are
dependent on the JavaFX framework, and utilize timelines. It's minimal external dependencies
makes the API portable and easy to use in other services.

* Easy to use and hard to misuse

The animation manager adheres to the Single Responsibility principle (managing the animation),
and is extremely user-friendly. The interface has four methods, with behaviors for starting,
pause/resume, and checking if the animation is paused. Furthermore, the aforementioned methods
are well-named, which significantly reduces the chance of misuse.

* Encapsulation and extension

Users of this class don't need to understand the underlying animation mechanism, making it less  
prone to misuse. This is a good example of encapsulation, as users do not need to know that
JavaFX timelines are being used, rather just some sort of animation. All the complexity
surrounding timeline scheduling, adding frames, and animation control is encapsulated away from
client code. The class can easily be extended to add new animation controls or event handling.

#### Changed

* Describe as Service

The `LanguageManager` API was in the view package and allowed for UI components to be displayed
in a variety of languages, to enhance game accessibility. The primary service of the API is to
enable multi-language support for UI components, so that game content can be displayed in
different languages without actually affecting the structure or design of the game.

* Easy to use and hard to misuse

We provided 3 different language options: English, Spanish, and French. However, adding a new  
language option is extremely easy, and simply requires adding a new button to the select
language  screen (ideally this would do auto re-formatting, but we did not have enough time for  
this), and then add a new properties file for the given language. Using the API itself is very
easy, as it only contains one public method: `getText`. This method takes in the language and a
tag (which corresponds to a key in the properties file), and retrieves the necessary String
associated with that tag. This API underwent significant changes throughout the coding process,
as it originally consisted of a giant switch statement and one public method for each language.
Now, with the use of the enum, the process is much more simplified and streamlined.

* Encapsulation and extension

The hardcoded file paths (englishPropertiesPath, spanishPropertiesPath, etc.) tightly couple the
LanguageManager class with specific file locations on the device. Any changes to these file
paths would require modifications directly within the class, potentially affecting its internal
behavior and breaking the codebase. This lack of encapsulation makes the class less flexible and
harder to maintain, and can even be considered a violation of Don't Repeat Yourself. The use of
the map from entry in the `SupportedLanguage` enum to the actual properties file is a good step
in the right direction, but it can be improved through refactoring. For example, we can refactor
it so it utilizes some general base path (perhaps as a constructor parameter) and then
subdirectories with specific languages, so that overall file paths can be updated without
requiring a change to the `LanguageManager` class itself.

### Feature Design Reviews

#### Good Example **teammate** implemented

* Design:

One feature that my teammates worked on that I am particularly impressed by is the
`CompositeElement` class. In the Composite design pattern, there's typically a base component
interface or abstract class that defines common operations for both individual objects and  
composites (i.e., objects that contain other objects). In the CompositeElement class, the  
VisualElement acts as the base component, while the CompositeElement itself (the entire space
where the game is being played on – left side of the screen).

* Evaluation:

I would consider the `CompositeElement` to be well-designed for a number of reasons. First of
all, the use of the `commandMap` to associate game object IDs with their associated visual
elements allows for the `CompositeElement` class to encapsulate all of its implementation
details from its client code. There is a clear separation of concerns present, which adheres to
the Single Responsibility Principle, as the `CompositeElement` class solely focuses on
organizing and keeping the individual `VisualElements` in sync, whereas the `VisualElements` are
in charge of updating themselves with each time step.


#### Needs Improvement Example **teammate** implemented

* Design:

One feature that I believe the team could have done a better job designing is the `GameBuilder`
module as a whole. The module contains 5 different classes, `VariableBuilder`,
`GameObjectBuilder`, `KeysBuilder`, `RulesBuilder`, and `PlayersBuilder`. They all override the
functional interface GameBuilder, and contain a singular void method  `buildGameField(GameData
gameData, List<T> gameField)`. Each of the methods are exactly the same, with the parameter
`gameField` being casted into its specific type from the generic-typed `T`.

* Evaluation

The use of downcasting in the many implementations of the `GameBuilder` interface is concerning
for a few reasons. First and foremost, it is a violation of the Interface Segregation Principle,
and implies that `GameBuilder` may need to be segregated into smaller, more client specific
interfaces. Additionally, this is a clear violation of Don't Repeat Yourself, as each
implementation is identical except the downcasting. Lastly, there is limited type checking and  
extensibility opportunities, as there creates a need to change pre-existing types and methods in
order to allow for new ones. This can be improved through the use of the Factory pattern
instead of the Builder pattern, as it could better encapsulate the instantiation of game-related
objects, and also include the segregation of the very wide `GameBuilder` interface into many
smaller ones.

