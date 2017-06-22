package springer.paint.plugin

import springer.paint.canvas.{CanvasDsl, DrawPoint, DrawSequence}
import springer.paint.plugin.HorizontalLinePlugin.HorizontalLine
import springer.paint.point.Point
import springer.paint.terminal.{Failure, Parser, Success}
import springer.paint.terminal.CommonParsers._
import springer.paint.terminal.Parser._

/**
  * Draw vertical lines
  */
object VerticalLinePlugin extends Plugin[Char] {

    case class VerticalLine(x: Int, fromY: Int, toY: Int)

    /**
      * The type of the new Command
      */
    type Command = VerticalLine

    /**
      * Interpret the command into a CanvasDsl
      */
    def interpret(line: Command): CanvasDsl[Char] = {
        val actions = Plugin.range(line.fromY, line.toY).map {
            y => DrawPoint(Point(line.x, y), 'x')
        }
        DrawSequence(actions.toList)
    }

        /**
          * Parse an user input into this command
          */
        def commandParser: Parser[Command] = {
            val intParser = times(int, 4).mapSuccess { success =>
                success.value match {
                    case x1 :: y1 :: x2 :: y2 :: tail if x1 == x2 =>
                    Success(VerticalLine(x1, y1, y2), success.tail)
                    case _ =>
                    Failure("Line not valid")
                }
            }

            single("L", intParser).flatten()
        }
}
