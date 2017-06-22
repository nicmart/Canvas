package springer.paint.plugin

import springer.paint.canvas.{Canvas, CanvasDsl}
import springer.paint.point.Point
import springer.paint.terminal.Parser

object FillPlugin extends Plugin[Char] {

    final case class Fill(from: Point, input: Char)

    /**
      * The type of the new Command
      */
    type Command = Fill

    /**
      * Interpret the command into a CanvasDsl
      */
    def interpret(command: Fill, canvas: Canvas[Char, _]): CanvasDsl[Char] = ???

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Fill] = ???
}
