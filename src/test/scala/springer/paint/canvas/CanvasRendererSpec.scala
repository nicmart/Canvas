package springer.paint.canvas

import springer.paint.point.Point
import springer.paint.spec.CommonSpec

class CanvasRendererSpec extends CommonSpec {
    "A Simple Canvas Renderer" should {
        "render a char canvas simply printing all the characters" in {
            val canvas = Canvas.filled(3, 2, ' ').drawPoint(Point(1, 1), 'x')
            SimpleCharCanvasRenderer.render(canvas) shouldBe "x  \n   "
        }
    }

    "A BorderCanvasRenderer" should {
        val renderer = BorderCanvasRenderer('-', '|', 'a', 'b', 'c', 'd')
        "Add a border to a char canvas" in {
            val canvas = Canvas.filled(3, 2, ' ').drawPoint(Point(1, 1), 'x')
            val expected =
                """a---b
                  ||x  |
                  ||   |
                  |d---c""".stripMargin
            renderer.render(canvas) shouldBe expected
        }
    }
}
