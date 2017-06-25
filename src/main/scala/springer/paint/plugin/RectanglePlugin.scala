package springer.paint.plugin

import springer.paint.canvas.{Canvas, CanvasDsl, DrawSequence}
import springer.paint.point.Point
import springer.paint.terminal.CommonParsers.{int, single}
import springer.paint.terminal.Parser
import springer.paint.terminal.Parser._

object RectanglePlugin extends CanvasFreePlugin[Char] {

    import HorizontalLinePlugin.HorizontalLine
    import VerticalLinePlugin.VerticalLine

    final case class Rectangle(upperLeft: Point, lowerRight: Point)

    /**
      * The type of the new Command
      */
    type Command = Rectangle

    /**
      * Return some help about this command
      *
      * @param commandSymbol The symbol this plugin is registered to in the painter
      */
    def description(commandSymbol: String): String =
        s"""
           |Rectangle command: draw a rectangle
           |Format: $commandSymbol x1 y1 x2 y2
         """.stripMargin.trim


    def toCanvasDsl(rect: Rectangle): CanvasDsl[Char] = {
        val Rectangle(Point(x1, y1), Point(x2, y2)) = rect

        // This is to allow any position of the two points
        val (minX, maxX) = (Math.min(x1, x2), Math.max(x1, x2))
        val (minY, maxY) = (Math.min(y1, y2), Math.max(y1, y2))

        val commands = if (minX == maxX) {
            List(VerticalLinePlugin.toCanvasDsl(VerticalLine(minX, minY, maxY)))
        } else if (minY == maxY) {
            List(HorizontalLinePlugin.toCanvasDsl(HorizontalLine(minY, minX, maxX)))
        } else {
            List(
                HorizontalLinePlugin.toCanvasDsl(HorizontalLine(minY, minX, maxX - 1)),
                VerticalLinePlugin.toCanvasDsl(VerticalLine(maxX, minY, maxY - 1)),
                HorizontalLinePlugin.toCanvasDsl(HorizontalLine(maxY, maxX, minX + 1)),
                VerticalLinePlugin.toCanvasDsl(VerticalLine(minX, maxY, minY + 1))
            )
        }
        DrawSequence(commands)
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Rectangle] = {
        val pointParser = combine(int, int)(Point)
        combine(pointParser, pointParser)(Rectangle)
    }
}
