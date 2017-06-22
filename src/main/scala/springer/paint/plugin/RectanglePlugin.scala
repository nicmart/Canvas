package springer.paint.plugin

import springer.paint.canvas.{Canvas, CanvasDsl, DrawSequence}
import springer.paint.point.Point
import springer.paint.terminal.CommonParsers.{int, single}
import springer.paint.terminal.Parser
import springer.paint.terminal.Parser.times

object RectanglePlugin extends Plugin[Char] {

    import HorizontalLinePlugin.HorizontalLine
    import VerticalLinePlugin.VerticalLine

    final case class Rectangle(upperLeft: Point, lowerRight: Point)

    /**
      * The type of the new Command
      */
    type Command = Rectangle

    /**
      * Interpret the command into a CanvasDsl
      */
    def interpret(rect: Rectangle, canvas: Canvas[Char, _]): CanvasDsl[Char] = {
        val Rectangle(Point(x1, y1), Point(x2, y2)) = rect

        // This is to allow any position of the two points
        val (minX, maxX) = (Math.min(x1, x2), Math.max(x1, x2))
        val (minY, maxY) = (Math.min(y1, y2), Math.max(y1, y2))

        val commands = if (minX == maxX) {
            List(VerticalLinePlugin.interpret(VerticalLine(minX, minY, maxY), canvas))
        } else if (minY == maxY) {
            List(HorizontalLinePlugin.interpret(HorizontalLine(minY, minX, maxX), canvas))
        } else {
            List(
                HorizontalLinePlugin.interpret(HorizontalLine(minY, minX, maxX - 1), canvas),
                VerticalLinePlugin.interpret(VerticalLine(maxX, minY, maxY - 1), canvas),
                HorizontalLinePlugin.interpret(HorizontalLine(maxY, maxX, minX + 1), canvas),
                VerticalLinePlugin.interpret(VerticalLine(minX, maxY, minY + 1), canvas)
            )
        }
        DrawSequence(commands)
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Rectangle] = {
        val intParser = times(int, 4) map { ints =>
            Rectangle(Point(ints(0), ints(1)), Point(ints(2), ints(3)))
        }
        single("R", intParser).flatten()
    }
}
