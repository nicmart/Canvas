package springer.paint.canvas

import springer.paint.point.Point
import springer.paint.spec.CommonSpec

class CanvasDslSpec extends CommonSpec {
    "A flatten canvas dsl expression" should {
        "contain only DrawPoint commands" in {
            val command = DrawSequence(List(DrawSequence(List(DrawPoint(Point(1, 1), 'x')))))
            DrawSequence.flatten(command) shouldBe DrawPoint(Point(1, 1), 'x')
        }
    }
}
