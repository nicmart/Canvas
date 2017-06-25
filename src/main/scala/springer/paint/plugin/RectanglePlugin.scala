package springer.paint.plugin

import springer.paint.canvas.{Canvas, CanvasDsl, DrawSequence}
import springer.paint.point.Point
import springer.paint.terminal.CommonParsers.{int, single}
import springer.paint.terminal.Parser
import springer.paint.terminal.Parser._

/**
  * A plugin that draws rectangles
  *
  * This is implemented syntactic sugar over horizontal and vertical lines
  *
  * @param hPlugin The plugin used to draw horizontal lines
  * @param vPlugin The plugin used to draw vertical lines
  * @tparam In
  */
case class RectanglePlugin[In](
    hPlugin: HorizontalLinePlugin[In],
    vPlugin: VerticalLinePlugin[In]
) extends CanvasFreePlugin[In] {

    import hPlugin.HorizontalLine
    import vPlugin.VerticalLine

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


    def toCanvasDsl(rect: Rectangle): CanvasDsl[In] = {
        val Rectangle(Point(x1, y1), Point(x2, y2)) = rect

        // This is to allow any position of the two points
        val (minX, maxX) = (Math.min(x1, x2), Math.max(x1, x2))
        val (minY, maxY) = (Math.min(y1, y2), Math.max(y1, y2))

        val commands = if (minX == maxX) {
            List(vPlugin.toCanvasDsl(VerticalLine(minX, minY, maxY)))
        } else if (minY == maxY) {
            List(hPlugin.toCanvasDsl(HorizontalLine(minY, minX, maxX)))
        } else {
            List(
                hPlugin.toCanvasDsl(HorizontalLine(minY, minX, maxX - 1)),
                vPlugin.toCanvasDsl(VerticalLine(maxX, minY, maxY - 1)),
                hPlugin.toCanvasDsl(HorizontalLine(maxY, maxX, minX + 1)),
                vPlugin.toCanvasDsl(VerticalLine(minX, maxY, minY + 1))
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
