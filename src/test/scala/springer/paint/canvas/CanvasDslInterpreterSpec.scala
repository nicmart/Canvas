package springer.paint.canvas

import springer.paint.point.Point
import springer.paint.spec.CommonSpec

class CanvasDslInterpreterSpec extends CommonSpec {
    "A Canvas Dsl Intepreter" should {
        val interpreter = new CanvasDslInterpreter[Char, String]

        "Draw single points on a canvas" in {
            val expr = DrawPoint(Point(1, 1), 'x')
            val canvas = interpreter.run(CharCanvas.empty(10, 10), expr)
            canvas.valueAt(Point(1, 1)) shouldBe Some('x')
            canvas.valueAt(Point(2, 2)) shouldBe Some(' ')
        }

        "Draw a sequence of points on a canvas" in {
            val expr = DrawSequence(List(DrawPoint(Point(1, 1), 'x'), DrawPoint(Point(1, 2), 'o')))
            val canvas = interpreter.run(CharCanvas.empty(10, 10), expr)
            canvas.valueAt(Point(1, 1)) shouldBe Some('x')
            canvas.valueAt(Point(1, 2)) shouldBe Some('o')
            canvas.valueAt(Point(2, 2)) shouldBe Some(' ')
        }
    }
}
