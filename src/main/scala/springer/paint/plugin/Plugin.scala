package springer.paint.plugin

import springer.paint.canvas.{Canvas => PaintCanvas, CanvasDsl}
import springer.paint.dsl.{Draw, PaintDsl}
import springer.paint.state.{Initialised, PaintState}
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
    final type State = PaintState[In, Out]
    final type Canvas = PaintCanvas[In, Out]
    final type StateTransition = State => State
    final type CanvasTransition = Canvas => Canvas

    /**
      * Interpret the command
      */
    def interpret(command: Command, state: State): State

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Command]

    /**
      * Parse user input and translate to a new state
      */
    def parser: Parser[StateTransition] =
        commandParser.map(command => interpret(command, _))
}

trait StateFreePlugin[In, Out] extends Plugin[In, Out] {
    def toCanvasDsl(command: Command): CanvasDsl[In]

    override def interpret(command: Command, state: State): State =
        state.mapCanvas(_.run(toCanvasDsl(command)))
}

trait CanvasSensitivePlugin[In, Out] extends Plugin[In, Out] {
    def toCanvasTransition(command: Command, canvas: Canvas): Canvas

    override def interpret(command: Command, state: State): State =
        state.mapCanvas(canvas => toCanvasTransition(command, canvas))
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
