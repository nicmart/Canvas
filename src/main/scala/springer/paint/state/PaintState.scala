package springer.paint.state

import springer.paint.canvas.Canvas

sealed trait PaintState[In, Out] {
    def withCanvas(canvas: Canvas[In, Out]): PaintState[In, Out] =
        Initialised(canvas)
    def isInitialised: Boolean = this match {
        case Initialised(_) => true
        case _ => false
    }
    def mapCanvas(f: Canvas[In, Out] => Canvas[In, Out]): PaintState[In, Out] = this match {
        case Initialised(canvas) => Initialised(f(canvas))
        case _ => this
    }
}

case class Uninitialised[In, Out]() extends PaintState[In, Out]
case class Initialised[In, Out](canvas: Canvas[In, Out]) extends PaintState[In, Out]


