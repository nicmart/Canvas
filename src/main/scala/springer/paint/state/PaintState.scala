package springer.paint.state

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
trait PaintState[Out] {
    def output: Out
    def size: Option[(Int, Int)]
    def hasCanvas: Boolean = size.isDefined

    def withCanvas(width: Int, height: Int): PaintState[Out]
}

/**
  * A Paint state whose output type is string
  */
case class StringPaintState(output: String, size: Option[(Int, Int)]) extends PaintState[String] {
    def withCanvas(width: Int, height: Int): PaintState[String] =
        copy(size = Some(width, height))
}

object StringPaintState {
    val empty: PaintState[String] = StringPaintState("", None)
}


