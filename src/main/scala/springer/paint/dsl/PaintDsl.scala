package springer.paint.dsl

/**
  * The main DSL
  */
sealed trait PaintDsl

final case class NewCanvas(width: Int, height: Int) extends PaintDsl
final case class HorizontalLine(y: Int, from: Int, to: Int) extends PaintDsl
