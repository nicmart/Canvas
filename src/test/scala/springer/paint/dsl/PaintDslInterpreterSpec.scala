package springer.paint.dsl

import springer.paint.canvas.{CanvasDslInterpreter, CharCanvas, DrawPoint}
import springer.paint.point.Point
import springer.paint.spec.CommonSpec
import springer.paint.state.{Initialised, Uninitialised}

class PaintDslInterpreterSpec extends CommonSpec {
    "A Paint Dsl Interpreter" should {
        val interpreter = new PaintDslInterpreter[Char, String](
            new CanvasDslInterpreter[Char, String],
            CharCanvas.empty
        )

        "Create new canvas on new canvas commands" in {
            val action = NewCanvas(30, 10)
            val initialState = Uninitialised[Char, String]()
            val newState = interpreter.run(initialState, action)
            inside(newState) {
                case Initialised(canvas) => canvas shouldBe CharCanvas.empty(30, 10)
            }
        }

        "Draw on canvas on Draw command" in {
            val action = Draw(DrawPoint(Point(1, 1), 'x'))
            val initialState = Initialised(CharCanvas.empty(10, 10))
            val newState = interpreter.run(initialState, action)
            inside(newState) {
                case Initialised(canvas) =>
                    canvas.valueAt(Point(1, 1)) shouldBe Some('x')
            }
        }
    }
}
