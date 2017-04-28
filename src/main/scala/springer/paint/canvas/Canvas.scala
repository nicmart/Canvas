package springer.paint.canvas

import springer.paint.point.Point

/**
  * Low-level Canvas Type. Provide only drawPoint primitive.
  */
trait Canvas[In, Out] {
    def width: Int
    def height: Int
    def drawPoint(position: Point, input: In): Canvas[In, Out]
    def output: Out
}

/**
  * A Canvas type whose input is a char and ouput a String
  */
case class CharCanvas(
    width: Int,
    height: Int,
    private val pixels: Vector[String]
) extends Canvas[Char, String] {
    def drawPoint(position: Point, input: Char): Canvas[Char, String] = {
        if (isPointInCanvas(position)) {
            var oldRow = pixels(position.y)
            val newRow = oldRow.substring(0, position.x) + input + oldRow.substring(position.x + 1, width)
            copy(pixels = pixels.updated(position.y, newRow))
        } else this
    }

    def output: String = pixels.mkString("\n")

    private def isPointInCanvas(p: Point): Boolean = {
        p.x >= 0 && p.x < width && p.y >= 0 && p.y < height
    }
}

object CharCanvas {
    def empty(width: Int, height: Int): CharCanvas =
        CharCanvas(
            width,
            height,
            Vector.fill(height)(" " * width)
        )
}