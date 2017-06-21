package springer.paint.plugin

import springer.paint.canvas.CanvasDsl
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

    /**
      * Interpret the command into a CanvasDsl
      */
    def interpret(command: Command): CanvasDsl[In]

    /**
      * Parse an user input into this command
      */
    def parser: Parser[Command]

    /**
      * Parse user input and translate it to de-sugared CanvasDsl
      */
    def canvasParser: Parser[CanvasDsl[In]] =
        parser.map(interpret)
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
