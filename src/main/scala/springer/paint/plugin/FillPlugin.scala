package springer.paint.plugin

import springer.paint.canvas.Canvas
import springer.paint.point.Point
import springer.paint.parser.CommonParsers._
import springer.paint.parser.Parser
import springer.paint.parser.Parser._

import scala.annotation.tailrec
import scala.collection.immutable.Queue

/**
  * Bucket-fill plugin.
  *
  * @param inputParser The parser that translates a string into an input
  * @tparam In The type of the input
  */
case class FillPlugin[In](inputParser: Parser[In]) extends CanvasPlugin[In] {

    final case class Fill(from: Point, input: In)

    /**
      * Return some help about this command
      *
      * @param commandSymbol The symbol this plugin is registered to in the painter
      */
    def description(commandSymbol: String): String =
        s"""
           |Bucket fill command: fill an area with a specific character {c}
           |Format: $commandSymbol x y {c}
        """.stripMargin.trim

    /**
      * The type of the new Command
      */
    type Command = Fill

    /**
      * Canvas transition for this command
      */
    override def transformCanvas[In2 >: In](command: Fill, canvas: Canvas[In2]): Canvas[In2] = {
        val Fill(from, newColor) = command
        canvas.valueAt(from) match {
            case None => canvas
            case Some(currentColor) =>
                fill(canvas, Queue.empty.enqueue(from), currentColor, newColor)
        }
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Fill] = {
        combine(point, inputParser)(Fill).finalise()
    }

    /**
      * The recursive-algorithm for bucket-fill
      * It's a breadth-first search on the graph where two points are connected
      * if they are contiguous and they have the same color
      */
    @tailrec private def fill[In2 >: In](
        canvas: Canvas[In2],
        points: Queue[Point],
        from: In2,
        to: In
    ): Canvas[In2] = {
        if (points.nonEmpty) {
            val (point, remainingPoints) = points.dequeue
            if (canvas.valueAt(point).contains(from)) {
                val newCanvas = canvas.drawPoint(point, to)
                val neighboursWithSameColors = newCanvas.neighboursOf(point).filter {
                    point => newCanvas.valueAt(point).contains(from)
                }
                val nextPoints = remainingPoints.enqueue(neighboursWithSameColors)
                fill(newCanvas, nextPoints, from, to)
            } else {
                fill(canvas, remainingPoints, from, to)
            }
        } else {
            canvas
        }
    }
}
