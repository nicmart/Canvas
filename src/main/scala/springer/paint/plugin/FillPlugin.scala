package springer.paint.plugin

import springer.paint.point.Point
import springer.paint.terminal.CommonParsers.{char, int, single}
import springer.paint.terminal.Parser
import springer.paint.terminal.Parser.combine

import scala.collection.immutable.Queue

object FillPlugin extends CanvasSensitivePlugin[Char, String] {

    final case class Fill(from: Point, input: Char)

    /**
      * The type of the new Command
      */
    type Command = Fill

    /**
      * Canvas transition for this command
      */
    def toCanvasTransition(command: Fill, canvas: Canvas): Canvas = {
        val Fill(from, value) = command
        canvas.valueAt(from) match {
            case None => canvas
            case Some(color) =>
                var newCanvas = canvas
                var queue: Queue[Point] = Queue.empty.enqueue(from)
                while (queue.nonEmpty) {
                    val (point, dequeued) = queue.dequeue
                    if (newCanvas.valueAt(point).contains(color)) {
                        newCanvas = newCanvas.drawPoint(point, value)
                        queue = dequeued.enqueue(newCanvas.neighboursOf(point).filter { point =>
                            newCanvas.valueAt(point).contains(color)
                        })
                    } else {
                        queue = dequeued
                    }
                }
                newCanvas
        }
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Fill] = {
        val pointParser = combine(int, int)(Point)
        val fillParser = combine(pointParser, char)(Fill)
        single("B", fillParser).flatten()
    }
}
