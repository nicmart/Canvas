package springer.paint.plugin

import springer.paint.point.Point
import springer.paint.terminal.CommonParsers._
import springer.paint.terminal.Parser
import springer.paint.terminal.Parser._

import scala.annotation.tailrec
import scala.collection.immutable.Queue

object FillPlugin extends CanvasPlugin[Char] {

    final case class Fill(from: Point, input: Char)

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
    def transformCanvas(command: Fill, canvas: Canvas): Canvas = {
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
        combine(point, char)(Fill)
    }

    @tailrec
    private def fill(
        canvas: Canvas,
        points: Queue[Point],
        fromChar: Char,
        toChar: Char
    ): Canvas = {
        if (points.nonEmpty) {
            val (point, remainingPoints) = points.dequeue
            if (canvas.valueAt(point).contains(fromChar)) {
                val newCanvas = canvas.drawPoint(point, toChar)
                val neighboursWithSameColors = newCanvas.neighboursOf(point).filter {
                    point => newCanvas.valueAt(point).contains(fromChar)
                }
                val nextPoints = remainingPoints.enqueue(neighboursWithSameColors)
                fill(newCanvas, nextPoints, fromChar, toChar)
            } else {
                fill(canvas, remainingPoints, fromChar, toChar)
            }
        } else {
            canvas
        }
    }
}
