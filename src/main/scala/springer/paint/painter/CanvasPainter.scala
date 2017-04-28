package springer.paint.painter
import springer.paint.canvas.{Canvas, CharCanvas}
import springer.paint.dsl.{HorizontalLine, NewCanvas, PaintDsl}
import springer.paint.point.Point
import springer.paint.state.{Initialised, PaintState}

/**
  * This is a painter that paints on a String
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
    }
}