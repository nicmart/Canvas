package springer.paint.plugin

import springer.paint.canvas.{Canvas => PaintCanvas, CanvasDsl}
import springer.paint.state.PaintState
import springer.paint.terminal.Parser

/**
  * With implementation of this trait we can extend behaviour of
  * our painter, introducing new commands.
  *
  * A command is specified by a type, and it is converted to
  * the Canvas Dsl through the interpret command.
  * The new command is then juPluginst syntactic sugar on top of the low-level Canvas Dsl
  *
  * The plugin also provide the parser that translate the user input into the new Command
  */
trait Plugin[In, Out] {
    /**
      * The type of the new Command
      */
    type Command

    /**
      * Interpret the command
      */
    def interpret(command: Command, state: State): State

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Command]

    def error: String = "Invalid format"
    def description: String = "Invalid format"

    /**
      * Parse user input and translate to a new state
      */
    def parser: Parser[StateTransition] =
        commandParser.map(command => interpret(command, _))

    final type State = PaintState[In, Out]
    final type Canvas = PaintCanvas[In, Out]
    final type StateTransition = State => State
    final type CanvasTransition = Canvas => Canvas
}

/**
  * A plugin for commands that draw on canvas.
  * The behaviour of these plugins do not depend on the current state of the canvas
  */
trait CanvasFreePlugin[In, Out] extends Plugin[In, Out] {
    /**
      * Convert the command to a canvas DSL
      */
    def toCanvasDsl(command: Command): CanvasDsl[In]

    override def interpret(command: Command, state: State): State =
        state.mapCanvas(_.run(toCanvasDsl(command)))
}

/**
  * A plugin that acts on a canvas and that depends on it
  */
trait CanvasPlugin[In, Out] extends Plugin[In, Out] {
    /**
      * Apply this command to the canvas
      */
    def transformCanvas(command: Command, canvas: Canvas): Canvas

    override def interpret(command: Command, state: State): State =
        state.mapCanvas(canvas => transformCanvas(command, canvas))
}

object Plugin {
    /**
      * A range where the elements can be given in any order
      */
    def range(start: Int, end: Int): Range = {
        if (start <= end) {
            start to end
        } else {
            start to end by -1
        }
    }
}
