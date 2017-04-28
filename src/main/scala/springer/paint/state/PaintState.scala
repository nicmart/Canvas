package springer.paint.state

/**
  * PaintState: define an output with a canvas size
  */
case class PaintState[Out](output: Out, size: Option[(Int, Int)]) {
    def hasCanvas: Boolean = size.isDefined
    def withCanvas(width: Int, height: Int): PaintState[Out] =
        copy(size = Some(width, height))
    def withOutput(newOutput: Out): PaintState[Out] =
        copy(output = newOutput)
}

object StringPaintState {
    val empty: PaintState[String] = PaintState[String]("", None)
}


