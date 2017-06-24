package springer.paint.plugin

import springer.paint.canvas.{Canvas, CanvasDsl, DrawPoint, DrawSequence}
import springer.paint.point.Point
import springer.paint.terminal.CommonParsers._
import springer.paint.terminal.Parser._
import springer.paint.terminal.{Failure, Parser, Success}

/**
  * Draw horizontal lines
  */
object HorizontalLinePlugin extends CanvasFreePlugin[Char, String] {

    final case class HorizontalLine(y: Int, fromX: Int, toX: Int)

    /**
      * The type of the new Command
      */
    type Command = HorizontalLine

    override def description: String =
        "Draw an horizontal line. Format: L x1 y1 "

    override def toCanvasDsl(line: HorizontalLine): CanvasDsl[Char] = {
        val actions = Plugin.range(line.fromX, line.toX).map {
            x => DrawPoint(Point(x, line.y), 'x')
        }
        DrawSequence(actions.toList)
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Command] = {
        pair(point, point).map {
            case (Point(x1, y1), Point(x2, y2)) if y1 == y2 =>
                HorizontalLine(y1, x1, x2)
        }
    }
}
