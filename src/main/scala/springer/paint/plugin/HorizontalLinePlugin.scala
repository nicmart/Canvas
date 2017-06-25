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

    /**
      * Return some help about this command
      *
      * @param commandSymbol The symbol this plugin is registered to in the painter
      */
    def description(commandSymbol: String): String =
        s"""
           |Horizontal Line command: draw an horizontal line
           |Format: $commandSymbol x1 y1 x2 y2, where y1 = y2
         """.stripMargin.trim

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
