# Paint
## Instructions
To run paint, just launch it with sbt:

```
sbt run
```

Tests can be run through sbt as well:

```
sbt test
```

## Behaviour decisions on edge cases
The requirements did not specify the behaviour on some edge-cases.
These are the decisions I took:

 1. It is possible to draw lines and rectangles whose edges are outside the canvas. Only the pixels in the canvas will be drawn.
 2. A line is treated like any other pixel. So it is possible to change the characters of a line applying the fill command
   on one of its points. So drawing (L 1 1 10 1) and then (B 1 1 o) will draw a line of "o" from (1, 1) to (10, 1) 
 3. Horizontal (vertical) lines can be expressed from right-to-left (top-down) or left-to-right (bottom-up)
 4. Rectangles can be expressed with any pair of points. The first point does not need to be the upper-left.
   

## Design
### Generic design decisions
In general I've tried to stay as much as possible a in a functional world, where side-effects and mutability are
 not allowed.
 
#### Immutability
In this project all the data structures are immutable. Even when we need to "update" something, a new copy
of that thing is created instead of updating it in place.

In tests this immutability, together with case classes, is exploited a lot, since it allows for all the components to have value-semantic,
making life much easier when comparing objects.

#### Purity
Almost all methods and functions are without side-effect.

The only component that has a side-effect on the external world is at the boundary of our system.
It is `PaintApp`, the object that implements the main application loop.

#### Canvas input type and variance
All the components abstract on the input type of "colors" for points. In this way it would be easy
to re-use the same components to implement a paint app that draws, instead of single characters on a terminal,
full RGB colors on a bitmap.

I've also tried to express the variance of all higher-kinded types.
This allows to define special states as objects, like the state `Final` (see below), that is a case object
for all kind canvases, (i.e. it is a `PaintState[Nothing]`).

### Canvas
The `Canvas` object is the low-level component that implements a single drawing primitive: drawing a point.
`Canvas` case class abstracts on the input type, so we can re-use the same implementation either to draw chracters
(our case), or for example colors encoded in java.awt.Color` objects.

Drawing a point does not mutate the canvas. Instead, a new canvas, with updated pixels, will be returned.

### Canvas Renderer
A `CanvasRenderer` is able to convert a canvas to something flat, like a `String`.
Our main Canvas Renderer is `BorderCanvasRenderer`, that adds some border around the canvas.

### PaintState
The state of the application is encoded in `PaintState`.
It is an algebraic data type whose cases are the states the application can be in. They are:

 1. `Uninitialised`: the initial state, when we do not have a canvas yet
 2. `Initialised`: when we have a canvas
 3. `Final`: the final state, after which the program exits
 4. `Output`: when there is some output to print
 
 Point 4. show how even the output is handled in a side-effect free way.
  Only at the end, in `PaintApp`, that output will be printed to the terminal.
  
### Parsers
There is a small parser library under the `springer.paint.parser` namespace.
The parser library is used inside `Plugins` (see below) to extract user input into structured commands.

Almost all the parsers are built composing elementary ones. For example this is a parser that parses 2 integers
  and return a `Point`:
  
```scala
import springer.paint.parser.CommonParsers._
import springer.paint.point.Point

val pointParser = combine(int, int)(Point)
```

Common parsers used in the plugins can be found in `springer.paint.parser.CommonParsers` object,
while combinators are in `springer.paint.parser.Parser` trait or companion object.
 
### Plugins
Plugins are the part of the app that defines command, and that make the system extensible.

These are the methods a plugin must implement:
```scala
trait Plugin[+In] {
    /**
      * The type of the new Command
      */
    type Command

    /**
      * Interpret the command
      */
    def interpret[In2 >: In](command: Command, state: PaintState[In2]): PaintState[In2]

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Command]
    
    // ...
}
```
As you can see, there are 3 main components that Plugin must define.

 1. A Command type: this is an internal, structured representation of the Command that the plugin creates before applying
 it. For example, in the `RectanglePlugin`, `Command` is the case class `Rectangle(Point(x1, y1), Point(x2, y2))`.
 2. A `Parser[Command]`, that tries to parse the string input entered by the user into `Command`.
 3. An "interpreter", that, given a command, evolves the `PaintState` to the next one.
   
### Painter
The painter glues plugins together giving the final method that evolves the state given an user input:
```scala
  def run(state: PaintState[In], input: String): PaintState[In] 
``` 

### App loop
Inside `PaintApp` is implemented the main application loop, that can be summarized as:

  1. Print output of the current state if there is any
  2. If the current state is final, exit.
  2. Build the next state processing the user input with `Painter.run`
  3. Print the canvas if there is any
  4. Start again

### Configuration
Configuration is defined in `springer.paint.app.config.Wiring`. That's were for example plugins are registered into the Painter:

```scala
lazy val painter = Painter[Char](Map.empty)
    .addPlugin("C", newCanvasPlugin)
    .addPlugin("L", horizontalLinePlugin)
    // ...
```

It is also possible to configure the characters for lines, or the characters for the border of the rendered canvas.