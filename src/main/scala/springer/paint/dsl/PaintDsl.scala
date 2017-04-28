package springer.paint.dsl

/**
  * The main DSL
  */
sealed trait PaintDsl

final case class NewCanvas(width: Int, height: Int) extends PaintDsl
