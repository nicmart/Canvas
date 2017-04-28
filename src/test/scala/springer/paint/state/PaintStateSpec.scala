package springer.paint.state

import springer.paint.canvas.CharCanvas
import springer.paint.point.Point
import springer.paint.spec.CommonSpec

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
class PaintStateSpec extends CommonSpec {

    "An unitialised PaintState" must {
        "say it is not initialised" in {
            val state = Uninitialised()
            state.isInitialised shouldBe false
        }
        "remain unchanged on canvas mapping" in {
            val state = Uninitialised[Any, Any]()
            state.mapCanvas(_.drawPoint(Point(0, 0), ???)) shouldBe state
        }
        "initialise canvas on withCanvas" in {
            val state = Uninitialised[Char, String]()
            state.withCanvas(CharCanvas.empty(20, 10)) shouldBe Initialised(CharCanvas.empty(20, 10))
        }
    }

    "An initialised PaintState" must {
        "say it is initialised" in {
            val state = Initialised(CharCanvas.empty(20, 10))
            state.isInitialised shouldBe true
        }
        "map underlying canvas on mapCanvas" in {
            val state = Initialised(CharCanvas.empty(20, 10))
            state.mapCanvas(_ => CharCanvas.empty(5, 5)) shouldBe Initialised(CharCanvas.empty(5, 5))
        }
    }
}
