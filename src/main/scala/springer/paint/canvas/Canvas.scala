package springer.paint.canvas

import springer.paint.point.Point

/**
  * Low-level Canvas Type. Provide only drawPoint primitive.
  */
trait Canvas[In] {
    /**
      * Canvas width
      */
    def width: Int

    /**
      * Canvas height
      */
    def height: Int

    /**
      * Draw a point on the canvas
      */
    def drawPoint(position: Point, input: In): Canvas[In]

    /**
      * Return all the pixels
      */
    def pixels: IndexedSeq[IndexedSeq[In]]

    /**
      * Get the value of a pixel on the canvas
      */
    def valueAt(position: Point): Option[In]

    /**
      * Get the neighbours of a point
      */
    def neighboursOf(point: Point): List[Point] = List(
        point + Point(0, 1),
        point + Point(1, 0),
        point + Point(0, -1),
        point + Point(-1, 0)
    ).filter(isPointInCanvas)

    /**
      * Is the point in this canvas?
      */
    def isPointInCanvas(p: Point): Boolean = {
        p.x >= 1 && p.x <= width && p.y >= 1 && p.y <= height
    }

    /**
      * Draw any CanvasDsl action
      */
    def run(action: CanvasDsl[In]): Canvas[In] =
        action match {
            case DrawPoint(point, in) => drawPoint(point, in)
            case DrawSequence(actions) => actions.foldLeft(this)(_.run(_))
        }
}

/**
  * A Canvas type whose input is a char and ouput a String
  */
case class CharCanvas(
    width: Int,
    height: Int,
    pixels: IndexedSeq[IndexedSeq[Char]]
) extends Canvas[Char] {
    /**
      * Draw a point on a canvas
      */
    def drawPoint(position: Point, input: Char): Canvas[Char] = {
        if (isPointInCanvas(position)) {
            val newRow = pixels(position.y - 1).updated(position.x - 1, input)
            val newPixels = pixels.updated(position.y - 1, newRow)
            copy(pixels = newPixels)
        } else this
    }

    def output: String = {
//        val line = "-" * (width + 2)
//        (line +:  pixels.map("|" + _ + "|") :+ line).mkString("\n")
        pixels.map(_.mkString("")).mkString("\n")
    }

    def valueAt(position: Point): Option[Char] =
        if (isPointInCanvas(position)) {
            Some(pixels(position.y - 1)(position.x - 1))
        } else {
            None
        }
}

object CharCanvas {
    def empty(width: Int, height: Int): CharCanvas =
        CharCanvas(width, height, IndexedSeq.fill(height, width)(' '))
}
