package springer.paint.painter

import springer.paint.canvas.CharCanvas
import springer.paint.dsl.NewCanvas
import springer.paint.spec.CommonSpec
import springer.paint.state.{Initialised, Uninitialised}

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
class CanvasPainterSpec extends CommonSpec {

    "A Canvas Painter" must {

        "draw an empty canvas when a canvas command is received" in {
            val initialState = Uninitialised[Char, String]()
            val painter = new CanvasPainter
            val command = NewCanvas(20, 10)
            val expectedOutput = List.fill(10)(" " * 20).mkString("\n")
            val finalState = painter(initialState, command)

            inside(painter(initialState, command)) {
                case Initialised(canvas) =>
                    canvas.output shouldBe expectedOutput
                    canvas.width shouldBe 20
                    canvas.height shouldBe 10
            }
        }

        "drawing a new canvas should override the previous one" in {
            val initialState = Initialised(CharCanvas.empty(20, 10))
            val painter = new CanvasPainter
            val canvas2 = NewCanvas(10, 10)
            val expectedOutput = List.fill(10)(" " * 10).mkString("\n")

            val finalState = painter(initialState, canvas2)
            inside(finalState) {
                case Initialised(canvas) =>
                    canvas.output shouldBe expectedOutput
                    canvas.width shouldBe 10
                    canvas.height shouldBe 10
            }
        }
    }

}
