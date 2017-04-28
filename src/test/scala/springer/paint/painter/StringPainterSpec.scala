package springer.paint.painter

import org.scalatest.{Matchers, WordSpec}
import springer.paint.dsl.Canvas
import springer.paint.state.StringPaintState

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
class StringPainterSpec extends WordSpec with Matchers {

    "A String Painter" must {
        "draw an empty canvas when a canvas command is received" in {
            val initialState = StringPaintState.empty
            val painter = new StringPainter
            val command = Canvas(20, 10)
            val expectedOutput = List.fill(10)(" " * 20).mkString("\n")

            painter(initialState, command).output shouldBe expectedOutput
        }
    }

}
