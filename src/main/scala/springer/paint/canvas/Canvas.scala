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
    def valueAt(position: Point): Option[In]

    def neighboursOf(position: Point): List[Point] = List(
        position + Point(0, 1),
        position + Point(1, 0),
        position + Point(0, -1),
        position + Point(-1, 0)
    ).filter(isPointInCanvas)

    def isPointInCanvas(p: Point): Boolean = {
        p.x >= 1 && p.x <= width && p.y >= 1 && p.y <= height
    }
}

/**
  * A Canvas type whose input is a char and ouput a String
  */
case class CharCanvas(
    width: Int,
    height: Int,
    private val pixels: Vector[String]
) extends Canvas[Char, String] {
    /**
      * Draw a point on a canvas
      */
    def drawPoint(position: Point, input: Char): Canvas[Char, String] = {
        if (isPointInCanvas(position)) {
            var oldRow = pixels(position.y - 1)
            val newRow = oldRow.substring(0, position.x - 1) + input + oldRow.substring(position.x, width)
            copy(pixels = pixels.updated(position.y - 1, newRow))
        } else this
    }

    def output: String =
        pixels.mkString("\n")
        //pixels.map(line => line.toCharArray.mkString(" ")).mkString("\n")

    def valueAt(position: Point): Option[Char] =
        if (isPointInCanvas(position)) {
            Some(pixels(position.y - 1)(position.x - 1))
        } else None
}

object CharCanvas {
    def empty(width: Int, height: Int): CharCanvas =
        CharCanvas(
            width,
            height,
            Vector.fill(height)(" " * width)
        )
}
