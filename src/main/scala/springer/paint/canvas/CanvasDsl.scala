package springer.paint.canvas

import springer.paint.point.Point

/**
  * Low level DSL for Canvas
  * In this language we can only express single-point actions
  * or a sequence of actions
  */
sealed trait CanvasDsl[+In]

/**
  * Draw a single point
  */
final case class DrawPoint[In](point: Point, value: In) extends CanvasDsl[In]

/**
  * Draw a sequence of actions
  */
final case class DrawSequence[In](actions: List[CanvasDsl[In]]) extends CanvasDsl[In]

object DrawSequence {
    /**
      * Flat nested sequences
      * Used mainly for testing purposes
      */
    def flatten[In](action: CanvasDsl[In]): CanvasDsl[In] = {
        def recFlat(action: CanvasDsl[In]): DrawSequence[In] = {
            DrawSequence(action match {
                case DrawPoint(_, _) => List(action)
                case DrawSequence(actions) => actions flatMap {
                    recFlat(_).actions
                }
            })
        }
        action match {
            case DrawPoint(_, _) => action
            case DrawSequence(List(singleAction)) => flatten(singleAction)
            case DrawSequence(_) => recFlat(action)
        }
    }
}