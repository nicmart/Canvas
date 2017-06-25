package springer.paint.canvas

import org.scalatest.{Matchers, WordSpec}
import springer.paint.point.Point
import springer.paint.spec.CommonSpec

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
class CanvasSpec extends CommonSpec {

    "A CharCanvas" must {
        "give an empty canvas when a new one is created" in {
            val canvas = CharCanvas.empty(20, 10)
            canvas.output shouldBe List.fill(10)(" " * 20).mkString("\n")
        }

        "draw a character in the right position" in {
            val canvas = CharCanvas.empty(4, 3)
                .drawPoint(Point(1, 1), 'x')
                .drawPoint(Point(2, 2), 'o')
                .drawPoint(Point(4, 3), 'o')
            canvas.output shouldBe "x   \n o  \n   o"
        }

        "get the value of a pixel" in {
            val canvas = CharCanvas.empty(4, 3)
                .drawPoint(Point(1, 1), 'x')
            canvas.valueAt(Point(1, 1)) shouldBe Some('x')
            canvas.valueAt(Point(1, 2)) shouldBe Some(' ')
            canvas.valueAt(Point(6, 2)) shouldBe None
        }

        "ignore out of canvas positions" in {
            val canvas = CharCanvas.empty(4, 3)
                .drawPoint(Point(0, 0), 'x')
                .drawPoint(Point(5, 1), 'o')
            canvas.output shouldBe "    \n    \n    "
        }

        "get the neighbours of a point" in {
            CharCanvas.empty(4, 3).neighboursOf(Point(1, 1)).toSet shouldBe List(
                Point(1, 2),
                Point(2, 1)
            ).toSet
            CharCanvas.empty(4, 3).neighboursOf(Point(2, 2)).toSet shouldBe List(
                Point(2, 1),
                Point(3, 2),
                Point(2, 3),
                Point(1, 2)
            ).toSet
            CharCanvas.empty(2, 2).neighboursOf(Point(2, 2)).toSet shouldBe List(
                Point(2, 1),
                Point(1, 2)
            ).toSet
        }

        "return all the pixels" in {
            val canvas = CharCanvas.empty(4, 3)
                .drawPoint(Point(1, 1), 'x')
                .drawPoint(Point(2, 2), 'y')
            canvas.pixels shouldBe IndexedSeq(
                IndexedSeq('x', ' ', ' ', ' '),
                IndexedSeq(' ', 'y', ' ', ' '),
                IndexedSeq(' ', ' ', ' ', ' ')
            )
        }
    }

}
