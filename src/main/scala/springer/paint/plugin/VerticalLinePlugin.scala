package springer.paint.plugin

import springer.paint.canvas.{Canvas, CanvasDsl, DrawPoint, DrawSequence}
import springer.paint.plugin.HorizontalLinePlugin.HorizontalLine
import springer.paint.point.Point
import springer.paint.terminal.{Failure, Parser, Success}
import springer.paint.terminal.CommonParsers._
import springer.paint.terminal.Parser._

/**
  * Draw vertical lines
  */
object VerticalLinePlugin extends CanvasFreePlugin[Char, String] {

    final case class VerticalLine(x: Int, fromY: Int, toY: Int)

    /**
      * The type of the new Command
      */
    type Command = VerticalLine

    /**
      * Interpret a Vertical Line as a CanvasDsl expression
      */
    override def toCanvasDsl(line: VerticalLine): CanvasDsl[Char] = {
        val actions = Plugin.range(line.fromY, line.toY).map {
            y => DrawPoint(Point(line.x, y), 'x')
        }
        DrawSequence(actions.toList)
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Command] = {
        pair(point, point).map {
            case (Point(x1, y1), Point(x2, y2)) if x1 == x2 =>
                VerticalLine(x1, y1, y2)
        }
    }
}
