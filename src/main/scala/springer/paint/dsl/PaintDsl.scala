package springer.paint.dsl

import springer.paint.point.Point

/**
  * The main DSL
  */
sealed trait PaintDsl

final case class NewCanvas(width: Int, height: Int) extends PaintDsl
final case class HorizontalLine(y: Int, from: Int, to: Int) extends PaintDsl
final case class VerticalLine(x: Int, from: Int, to: Int) extends PaintDsl
final case class Rectangle(upperLeft: Point, lowerRight: Point) extends PaintDsl
