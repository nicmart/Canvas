package springer.paint.plugin

import springer.paint.canvas.Canvas
import springer.paint.point.Point
import springer.paint.parser.CommonParsers.int
import springer.paint.parser.Parser
import springer.paint.parser.Parser._

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
) extends CanvasPlugin[In] {

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


    /**
      * Apply this command to the canvas
      */
    override
    def transformCanvas[In2 >: In](rect: Rectangle, canvas: Canvas[In2]): Canvas[In2] = {
        val Rectangle(Point(x1, y1), Point(x2, y2)) = rect

        // This is to allow any position of the two points
        val (minX, maxX) = (Math.min(x1, x2), Math.max(x1, x2))
        val (minY, maxY) = (Math.min(y1, y2), Math.max(y1, y2))

        if (minX == maxX) {
            vPlugin.transformCanvas(VerticalLine(minX, minY, maxY), canvas)
        } else if (minY == maxY) {
            hPlugin.transformCanvas(HorizontalLine(minX, minY, maxY), canvas)
        } else {
            val transformations = List[Canvas[In2] => Canvas[In2]](
                hPlugin.transformCanvas(HorizontalLine(minY, minX, maxX - 1), _),
                vPlugin.transformCanvas(VerticalLine(maxX, minY, maxY - 1), _),
                hPlugin.transformCanvas(HorizontalLine(maxY, maxX, minX + 1), _),
                vPlugin.transformCanvas(VerticalLine(minX, maxY, minY + 1), _)
            )
            transformations.foldLeft(canvas) {
                (currentCanvas, transformation) => transformation(currentCanvas)
            }
        }
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Rectangle] = {
        val pointParser = combine(int, int)(Point)
        combine(pointParser, pointParser)(Rectangle).finalise()
    }
}
