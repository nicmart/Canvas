package springer.paint.plugin

import springer.paint.canvas.Canvas
import springer.paint.point.Point
import springer.paint.parser.{Failure, Parser, Success}
import springer.paint.parser.CommonParsers._
import springer.paint.parser.Parser._

/**
  * Draw vertical lines
  */
case class VerticalLinePlugin[In](symbol: In) extends CanvasPlugin[In] {

    final case class VerticalLine(x: Int, fromY: Int, toY: Int)

    /**
      * The type of the new Command
      */
    type Command = VerticalLine

    /**
      * Return some help about this command
      *
      * @param commandSymbol The symbol this plugin is registered to in the painter
      */
    def description(commandSymbol: String): String =
        s"""
           |Vertical Line command: draw a vertical line
           |Format: $commandSymbol x1 y1 x2 y2, where x1 = x2
         """.stripMargin.trim


    /**
      * Apply this command to the canvas
      */
    override
    def transformCanvas[In2 >: In](line: VerticalLine, canvas: Canvas[In2]): Canvas[In2] = {
        Plugin.range(line.fromY, line.toY).foldLeft(canvas) {
            (currentCanvas, y) => currentCanvas.drawPoint(Point(line.x, y), symbol)
        }
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Command] = {
        pair(point, point).map {
            case (Point(x1, y1), Point(x2, y2)) if x1 == x2 =>
                VerticalLine(x1, y1, y2)
        }.finalise()
    }
}
