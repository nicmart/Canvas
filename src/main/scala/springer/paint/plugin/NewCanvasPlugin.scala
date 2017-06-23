package springer.paint.plugin

import springer.paint.canvas.CharCanvas
import springer.paint.state.{Initialised, PaintState}
import springer.paint.terminal.CommonParsers.{positiveInt, single}
import springer.paint.terminal.Parser
import springer.paint.terminal.Parser.times

object NewCanvasPlugin extends Plugin[Char, String] {

    final case class NewCanvas(width: Int, height: Int)

    /**
      * The type of the new Command
      */
    type Command = NewCanvas

    /**
      * Interpret the command
      */
    def interpret(command: NewCanvas, state: PaintState[Char, String]): PaintState[Char, String] =
        Initialised(CharCanvas.empty(command.width, command.height))

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[NewCanvas] = {
        val ints = times(positiveInt, 2).map(ints => NewCanvas(ints(0), ints(1)))
        single("C", ints).flatten()
    }
}
