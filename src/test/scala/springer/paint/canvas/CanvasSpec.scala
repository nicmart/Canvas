package springer.paint.canvas

import org.scalatest.{Matchers, WordSpec}
import springer.paint.point.Point

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
class CanvasSpec extends WordSpec with Matchers {

    "A CharCanvas" must {
        "give an empty canvas when a new one is created" in {
            val canvas = CharCanvas.empty(20, 10)
            canvas.output shouldBe List.fill(10)(" " * 20).mkString("\n")
        }

        "draw a character in the right position" in {
            val canvas = CharCanvas.empty(4, 3)
                .drawPoint(Point(0, 0), 'x')
                .drawPoint(Point(1, 1), 'o')
                .drawPoint(Point(3, 2), 'o')
            canvas.output shouldBe "x   \n o  \n   o"
        }

        "ignore out of canvas positions" in {
            val canvas = CharCanvas.empty(4, 3)
                .drawPoint(Point(-1, 0), 'x')
                .drawPoint(Point(4, 0), 'o')
            canvas.output shouldBe "    \n    \n    "
        }
    }

}
