package springer.paint.plugin

import springer.paint.canvas.{CanvasDsl, DrawPoint, DrawSequence}
import springer.paint.point.Point
import springer.paint.terminal.CommonParsers._
import springer.paint.terminal.Parser._
import springer.paint.terminal.{Failure, Parser, Success}

/**
  * Draw horizontal lines
  */
object HorizontalLinePlugin extends Plugin[Char] {

    case class HorizontalLine(y: Int, from: Int, to: Int)

    /**
      * The type of the new Command
      */
    type Command = HorizontalLine

    /**
      * Interpret the command into a CanvasDsl
      */
    def interpret(line: Command): CanvasDsl[Char] = {
        val actions = Plugin.range(line.from, line.to).map {
            x => DrawPoint(Point(x, line.y), 'x')
        }
        DrawSequence(actions.toList)
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Command] = {
        val intParser = times(int, 4).mapSuccess { success =>
            success.value match {
                case x1 :: y1 :: x2 :: y2 :: tail if y1 == y2 =>
                    Success(HorizontalLine(y1, x1, x2), success.tail)
                case _ =>
                    Failure("Line not valid")
            }
        }

        single("L", intParser).flatten()
    }
}
