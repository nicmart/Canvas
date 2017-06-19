package springer.paint.painter
import springer.paint.canvas.{Canvas, CharCanvas}
import springer.paint.dsl._
import springer.paint.point.Point
import springer.paint.state.{Initialised, PaintState, Uninitialised}

import scala.annotation.tailrec
import scala.collection.immutable.Queue

/**
  * This is a painter that paints on a CharCanvas
  */
class CanvasPainter extends Painter[Char, String] {

    type State = PaintState[Char, String]
    def apply(state: State, command: PaintDsl): State = command match {

        case NewCanvas(width, height) =>
            Initialised(CharCanvas.empty(width, height))

        case HorizontalLine(y, fromX, toX) =>
            range(fromX, toX).foldLeft(state) {
                case (s, x) => s.mapCanvas(_.drawPoint(Point(x, y), 'x'))
            }

        case VerticalLine(x, fromY, toY) =>
            range(fromY, toY).foldLeft(state) {
                case (s, y) => s.mapCanvas(_.drawPoint(Point(x, y), 'x'))
            }

        case Rectangle(upperLeft, lowerRight) => sequence(state) (
            HorizontalLine(upperLeft.y, upperLeft.x, lowerRight.x),
            VerticalLine(lowerRight.x, upperLeft.y, lowerRight.y),
            HorizontalLine(lowerRight.y, upperLeft.x, lowerRight.x),
            VerticalLine(upperLeft.x, upperLeft.y, lowerRight.y)
        )

        case Fill(from, value) => state.mapCanvas { canvas =>
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

    }

    /**
      * Apply a sequence of commands to a state
      */
    @tailrec
    private def sequence(state: State)(commands: PaintDsl*): State = {
        if (commands.isEmpty) state
        else {
            sequence(apply(state, commands.head))(commands.tail: _*)
        }
    }

    /**
      * A range where the elements can be given in any order
      */
    private def range(start: Int, end: Int): Range = {
        if (start <= end) {
            start to end
        } else {
            start to end by -1
        }
    }
}