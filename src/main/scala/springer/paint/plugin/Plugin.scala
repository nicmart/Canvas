package springer.paint.plugin

import springer.paint.canvas.{Canvas, CanvasDsl}
import springer.paint.dsl.{Draw, PaintDsl}
import springer.paint.state.{Initialised, PaintState}
import springer.paint.terminal.Parser

/**
  * With implementation of this trait we can extend behaviour of
  * our painter, introducing new commands.
  *
  * A command is specified by a type, and it is converted to
  * the Canvas Dsl through the interpret command.
  * The new command is then just syntactic sugar on top of the low-level Canvas Dsl
  *
  * The plugin also provide the parser that translate the user input into the new Command
  */
trait Plugin[In] {
    /**
      * The type of the new Command
      */
    type Command

    type State[Out] = PaintState[In, Out]
    type StateTransition[Out] = State[Out] => State[Out]

    /**
      * Interpret the command into a CanvasDsl
      */
    def interpret[Out](command: Command, state: PaintState[In, Out]): PaintState[In, Out]

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Command]

    /**
      * Parse user input and translate to a new state
      */
    def parser[Out]: Parser[PaintState[In, Out] => PaintState[In, Out]] =
        commandParser.map(command => interpret[Out](command, _))
}

trait StateFreePlugin[In] extends Plugin[In] {
    def toCanvasDsl(command: Command): CanvasDsl[In]

    override def interpret[Out](
        command: Command,
        state: PaintState[In, Out]
    ): PaintState[In, Out] = {
        if (state.isInitialised) {
            state.mapCanvas(_.run(toCanvasDsl(command)))
        } else {
            state.addMessage("Please create a canvas before running this command")
        }
    }
}

trait CanvasSensitivePlugin[In] extends Plugin[In] {
    def toCanvasTransition[Out](command: Command, canvas: Canvas[In, Out]): Canvas[In, Out]

    override def interpret[Out](
        command: Command,
        state: PaintState[In, Out]
    ): PaintState[In, Out] = {
        if (state.isInitialised) {
            state.mapCanvas(canvas => toCanvasTransition(command, canvas))
        } else {
            state.addMessage("Please create a canvas before running this command")
        }
    }
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
