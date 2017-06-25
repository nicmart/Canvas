package springer.paint.canvas

/**
  * Render a canvas into an output representation
  */
trait CanvasRenderer[-In, +Out] {
    /**
      * Render the canvas
      */
    def render(canvas: Canvas[In]): Out
}

/**
  * This just print the pixels without any further decoration
  */
object SimpleCharCanvasRenderer extends CanvasRenderer[Char, String] {
    /**
      * Render the canvas
      */
    def render(canvas: Canvas[Char]): String =
        canvas.pixels.map(_.mkString("")).mkString("\n")
}

/**
  * Render a canvas with an additional border
  */
final case class BorderCanvasRenderer(
    horizontal: Char,
    vertical: Char,
    topLeft: Char,
    topRight: Char,
    bottomRight: Char,
    bottomLeft: Char
) extends CanvasRenderer[Char, String] {
    /**
      * Render the canvas
      */
    def render(canvas: Canvas[Char]): String = {
        val horizontalPixels = horizontal.toString * canvas.width
        val lineTop = topLeft + horizontalPixels + topRight
        val lineBottom = bottomLeft + horizontalPixels + bottomRight
        val middleLines = canvas.pixels.map(vertical + _.mkString("") + vertical)
        (lineTop +: middleLines :+ lineBottom).mkString("\n")
    }
}
