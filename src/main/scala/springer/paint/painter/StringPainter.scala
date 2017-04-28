package springer.paint.painter
import springer.paint.dsl.{NewCanvas, PaintDsl}
import springer.paint.state.PaintState

/**
  * This is a painter that paints on a String
  */
class StringPainter(emptyChar: Char = ' ', lineSeparator: String = "\n") extends Painter[String] {
    def apply(state: PaintState[String], command: PaintDsl): PaintState[String] = command match {

        case NewCanvas(width, height) =>
            val output = List.fill(height)(emptyChar.toString * width).mkString(lineSeparator)
            state.withOutput(output).withCanvas(width, height)
    }
}
