package springer.paint.plugin

import springer.paint.canvas.{Canvas, CanvasDsl, DrawPoint, DrawSequence}
import springer.paint.point.Point
import springer.paint.terminal.CommonParsers._
import springer.paint.terminal.Parser._
import springer.paint.terminal.{Failure, Parser, Success}

/**
  * Draw horizontal lines
  */
object HorizontalLinePlugin extends Plugin[Char] {

    final case class HorizontalLine(y: Int, fromX: Int, toX: Int)

    /**
      * The type of the new Command
      */
    type Command = HorizontalLine

    /**
      * Interpret the command into a CanvasDsl
      */
    def interpret(line: Command, canvas: Canvas[Char, _]): CanvasDsl[Char] = {
        val actions = Plugin.range(line.fromX, line.toX).map {
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
