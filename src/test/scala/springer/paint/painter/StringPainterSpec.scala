package springer.paint.painter

import org.scalatest.{Matchers, WordSpec}
import springer.paint.dsl.NewCanvas
import springer.paint.state.StringPaintState

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
class StringPainterSpec extends WordSpec with Matchers {

    "A String Painter" must {
        "give empty size if no canvas has been created" in {
            val initialState = StringPaintState.empty
            initialState.size shouldBe None
        }

        "draw an empty canvas when a canvas command is received" in {
            val initialState = StringPaintState.empty
            val painter = new StringPainter
            val command = NewCanvas(20, 10)
            val expectedOutput = List.fill(10)(" " * 20).mkString("\n")
            val finalState = painter(initialState, command)

            painter(initialState, command).output shouldBe expectedOutput
            finalState.size shouldBe Some((20, 10))
        }

        "drawing a new canvas should override the previous one" in {
            val initialState = StringPaintState.empty
            val painter = new StringPainter
            val canvas1 = NewCanvas(20, 10)
            val canvas2 = NewCanvas(10, 10)
            val expectedOutput = List.fill(10)(" " * 10).mkString("\n")

            val finalState = painter(painter(initialState, canvas1), canvas2)
            finalState.output shouldBe expectedOutput
            finalState.size shouldBe Some((10, 10))
        }
    }

}
