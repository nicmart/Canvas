package springer.paint.canvas

import springer.paint.point.Point

/**
  * Low-level Canvas Type. Provide only drawPoint primitive.
  */
case class Canvas[+In] (
    width: Int,
    height: Int,
    pixels: IndexedSeq[IndexedSeq[In]]
) {
    /**
      * Draw a point on a canvas
      */
    def drawPoint[In2 >: In](position: Point, input: In2): Canvas[In2] = {
        if (isPointInCanvas(position)) {
            val newRow = pixels(position.y - 1).updated(position.x - 1, input)
            val newPixels = pixels.updated(position.y - 1, newRow)
            copy(pixels = newPixels)
        } else this
    }

    /**
      * Return the value at a given point
      */
    def valueAt(position: Point): Option[In] =
        if (isPointInCanvas(position)) {
            Some(pixels(position.y - 1)(position.x - 1))
        } else {
            None
        }

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
    def run[In2 >: In](action: CanvasDsl[In2]): Canvas[In2] =
        action match {
            case DrawPoint(point, in) => drawPoint(point, in)
            case DrawSequence(actions) => actions.foldLeft[Canvas[In2]](this)(_.run(_))
        }
}

object Canvas {
    def filled[In](width: Int, height: Int, input: In): Canvas[In] =
        Canvas(width, height, IndexedSeq.fill(height, width)(input))
}
