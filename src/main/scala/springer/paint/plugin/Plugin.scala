package springer.paint.plugin

import springer.paint.canvas.{Canvas, CanvasDsl}
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

    /**
      * Return some help about this command
      *
      * @param commandSymbol The symbol this plugin is registered to in the painter
      */
    def description(commandSymbol: String): String

    /**
      * Parse user input and translate to a new state
      */
    def parser[In2 >: In]: Parser[PaintState[In2] => PaintState[In2]] =
        commandParser.map(command => interpret(command, _))
}

/**
  * A plugin for commands that draw on canvas.
  * The behaviour of these plugins do not depend on the current state of the canvas
  */
trait CanvasFreePlugin[In] extends Plugin[In] {
    /**
      * Convert the command to a canvas DSL
      */
    def toCanvasDsl(command: Command): CanvasDsl[In]

    override def interpret[In2 >: In](command: Command, state: PaintState[In2]): PaintState[In2] = {
        if (state.isInitialised) {
            state.mapCanvas(_.run(toCanvasDsl(command)))
        } else {
            state.addOutput("This command is available only after a canvas is created.")
        }
    }
}

/**
  * A plugin that acts on a canvas and that depends on it
  */
trait CanvasPlugin[In] extends Plugin[In] {
    /**
      * Apply this command to the canvas
      */
    def transformCanvas[In2 >: In](command: Command, canvas: Canvas[In2]): Canvas[In2]

    override def interpret[In2 >: In](command: Command, state: PaintState[In2]): PaintState[In2] = {
        if (state.isInitialised) {
            state.mapCanvas[In2](canvas => transformCanvas(command, canvas))
        } else {
            state.addOutput("This command is available only after a canvas is created.")
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
