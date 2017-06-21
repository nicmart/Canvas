package springer.paint.dsl

import springer.paint.canvas.CanvasDsl
import springer.paint.point.Point

sealed trait PaintDsl[+In]
final case class NewCanvas(width: Int, height: Int) extends PaintDsl[Nothing] {
    assert(width > 0)
    assert(height > 0)
}
final case class Draw[In](action: CanvasDsl[In]) extends PaintDsl[In]