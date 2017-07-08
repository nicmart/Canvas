package springer.paint.state

import springer.paint.canvas.Canvas

import scala.collection.immutable.Queue

/**
  * An ADT for paint state.
  *
  * PaintState is a closed list of states
  */
sealed trait PaintState[+In] {

    /**
      * Tell if the state is initialised, i.e. if  we have a canvas
      */
    def isInitialised: Boolean = this match {
        case Initialised(_, _) => true
        case Output(_, Initialised(_, _)) => true
        case _ => false
    }

    /**
      * Tell if the state is the final state
      */
    def isFinal: Boolean = this match {
        case Final => true
        case _ => false
    }

    /**
      * Apply a transformation to a canvas, if we are in an initialised state
      */
    def mapCanvas[In2 >: In](f: Canvas[In] => Canvas[In2]): PaintState[In2] = this match {
        case Initialised(canvas, _) => Initialised(f(canvas))
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

    def next[In2 >: In](state: PaintState[In2]): PaintState[In2] = state match {
        case Initialised(canvas, history) => Initialised(canvas, this :: history)
        case _ => state
    }
}

/**
  * The initial state of the app, when we do not have a canvas yet
  */
case object Uninitialised extends PaintState[Nothing]

/**
  * An initialised state, i.e. a state with a canvas
  */
final case class Initialised[In](canvas: Canvas[In], history: List[PaintState[In]] = Nil) extends PaintState[In]

/**
  * The final state, after which the program halts
  */
case object Final extends PaintState[Nothing]

/**
  * Output state, in which there is some output to print
  */
final case class Output[In](messages: Queue[String], state: PaintState[In]) extends PaintState[In]

