package springer.paint.dsl

import springer.paint.point.Point

/**
  * The main DSL
  */
sealed trait PaintDslLegacy

final case class NewCanvasLegacy(width: Int, height: Int) extends PaintDslLegacy
final case class HorizontalLine(y: Int, from: Int, to: Int) extends PaintDslLegacy
final case class VerticalLine(x: Int, from: Int, to: Int) extends PaintDslLegacy
final case class Rectangle(upperLeft: Point, lowerRight: Point) extends PaintDslLegacy
final case class Fill(from: Point, input: Char) extends PaintDslLegacy
