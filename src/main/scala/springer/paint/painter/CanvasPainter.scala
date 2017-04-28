package springer.paint.painter
import springer.paint.canvas.{Canvas, CharCanvas}
import springer.paint.dsl._
import springer.paint.point.Point
import springer.paint.state.{Initialised, PaintState}

import scala.annotation.tailrec

/**
  * This is a painter that paints on a CharCanvas
  */
class CanvasPainter extends Painter[Char, String] {

    type State = PaintState[Char, String]
    def apply(state: State, command: PaintDsl): State = command match {

        case NewCanvas(width, height) =>
            Initialised(CharCanvas.empty(width, height))

        case HorizontalLine(y, fromX, toX) =>
            (fromX to toX).foldLeft(state) {
                case (s, x) => s.mapCanvas(_.drawPoint(Point(x, y), 'x'))
            }

        case VerticalLine(x, fromY, toY) =>
            (fromY to toY).foldLeft(state) {
                case (s, y) => s.mapCanvas(_.drawPoint(Point(x, y), 'x'))
            }

        case Rectangle(upperLeft, lowerRight) => sequence(state) (
            HorizontalLine(upperLeft.y, upperLeft.x, lowerRight.x),
            VerticalLine(lowerRight.x, upperLeft.y, lowerRight.y),
            HorizontalLine(lowerRight.y, upperLeft.x, lowerRight.x),
            VerticalLine(upperLeft.x, upperLeft.y, lowerRight.y)
        )

    }

    @tailrec
    private def sequence(state: State)(commands: PaintDsl*): State = {
        if (commands.isEmpty) state
        else {
            sequence(apply(state, commands.head))(commands.tail: _*)
        }
    }
}