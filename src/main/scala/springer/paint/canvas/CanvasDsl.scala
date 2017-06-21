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