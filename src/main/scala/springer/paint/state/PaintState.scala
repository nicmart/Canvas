package springer.paint.state

import springer.paint.canvas.Canvas

import scala.collection.immutable.Queue

sealed trait PaintState[In, Out] {
    /**
      * Set the canvas in the state
      */
    def withCanvas(canvas: Canvas[In, Out]): PaintState[In, Out] =
        Initialised(canvas)

    /**
      * Tell if the state is initialised, i.e. if  we have a canvas
      */
    def isInitialised: Boolean = this match {
        case Initialised(_) => true
        case _ => false
    }

    /**
      * Tell if the state is the final state
      */
    def isFinal: Boolean = this match {
        case Final() => true
        case _ => false
    }

    /**
      * Apply a transformation to a canvas, if we are in an initialised state
      */
    def mapCanvas(f: Canvas[In, Out] => Canvas[In, Out]): PaintState[In, Out] = this match {
        case Initialised(canvas) => Initialised(f(canvas))
        case _ => this
    }
}

final case class Uninitialised[In, Out]() extends PaintState[In, Out]
final case class Initialised[In, Out](canvas: Canvas[In, Out]) extends PaintState[In, Out]
final case class Final[In, Out]() extends PaintState[In, Out]


