package springer.paint.state

import springer.paint.canvas.Canvas

import scala.collection.immutable.Queue

sealed trait PaintState[In, Out] {
    def messages: Queue[String]
    def withCanvas(canvas: Canvas[In, Out]): PaintState[In, Out] =
        Initialised(canvas)
    def isInitialised: Boolean = this match {
        case Initialised(_, msgs) => true
        case _ => false
    }
    def mapCanvas(f: Canvas[In, Out] => Canvas[In, Out]): PaintState[In, Out] = this match {
        case Initialised(canvas, msgs) => Initialised(f(canvas))
        case _ => this
    }
    def addMessage(message: String): PaintState[In, Out] = this match {
        case Uninitialised(messages) => Uninitialised(messages.enqueue(message))
        case Initialised(canvas: Canvas[In, Out], messages) => Initialised(canvas, messages.enqueue(message))
    }
}

case class Uninitialised[In, Out](messages: Queue[String] = Queue.empty) extends PaintState[In, Out]
case class Initialised[In, Out](canvas: Canvas[In, Out], messages: Queue[String] = Queue.empty) extends PaintState[In, Out]


