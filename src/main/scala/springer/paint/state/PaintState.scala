package springer.paint.state

import springer.paint.canvas.Canvas

sealed trait PaintState[In, Out] {
    def withCanvas(canvas: Canvas[In, Out]): PaintState[In, Out] =
        Initialised(canvas)
    def isInitialised: Boolean = this match {
        case Initialised(_) => true
        case _ => false
    }
    def mapCanvas[In2, Out2](f: Canvas[In, Out] => Canvas[In2, Out2]): PaintState[In2, Out2] = this match {
        case Initialised(canvas) => Initialised(f(canvas))
    }
}

case class Uninitialised[In, Out]() extends PaintState[In, Out]
case class Initialised[In, Out](canvas: Canvas[In, Out]) extends PaintState[In, Out]


