package springer.paint.dsl

/**
  * The main DSL
  */
sealed trait PaintDsl

final case class Canvas(width: Int, height: Int) extends PaintDsl