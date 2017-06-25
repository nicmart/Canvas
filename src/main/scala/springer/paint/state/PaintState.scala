package springer.paint.state

import springer.paint.canvas.Canvas

import scala.collection.immutable.Queue

sealed trait PaintState[In] {
    /**
      * Set the canvas in the state
      */
    def withCanvas(canvas: Canvas[In]): PaintState[In] =
        Initialised(canvas)

    /**
      * Tell if the state is initialised, i.e. if  we have a canvas
      */
    def isInitialised: Boolean = this match {
        case Initialised(_) => true
        case Output(_, Initialised(_)) => true
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
    def mapCanvas(f: Canvas[In] => Canvas[In]): PaintState[In] = this match {
        case Initialised(canvas) => Initialised(f(canvas))
        case Output(messages, state) => Output(messages, state.mapCanvas(f))
        case _ => this
    }

    /**
      * Add an output to the state
      */
    def addOutput(output: String): PaintState[In] = this match {
        case Output(messages, state) => Output(messages.enqueue(output), state)
        case _ => Output(Queue(output), this)
    }

    /**
      * Consume the output, and return the next state
      */
    def consumeOutput: (Queue[String], PaintState[In]) = this match {
        case Output(messages, state) => (messages, state)
        case _ => (Queue.empty, this)
    }
}

final case class Uninitialised[In]() extends PaintState[In]
final case class Initialised[In](canvas: Canvas[In]) extends PaintState[In]
final case class Final[In]() extends PaintState[In]
final case class Output[In](messages: Queue[String], state: PaintState[In]) extends PaintState[In]


