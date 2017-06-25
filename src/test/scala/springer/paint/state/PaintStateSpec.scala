package springer.paint.state

import springer.paint.canvas.Canvas
import springer.paint.point.Point
import springer.paint.spec.CommonSpec

import scala.collection.immutable.Queue

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
class PaintStateSpec extends CommonSpec {

    "An unitialised PaintState" must {
        "say it is not initialised" in {
            val state = Uninitialised
            state.isInitialised shouldBe false
        }

        "remain unchanged on canvas mapping" in {
            val state = Uninitialised
            state.mapCanvas(_.drawPoint(Point(0, 0), ???)) shouldBe state
        }

        "initialise canvas on withCanvas" in {
            val state = Uninitialised
            state.withCanvas(Canvas.filled(20, 10, ' ')) shouldBe Initialised(Canvas.filled(20, 10, ' '))
        }
    }

    "An initialised PaintState" must {
        "say it is initialised" in {
            val state = Initialised(Canvas.filled(20, 10, ' '))
            state.isInitialised shouldBe true
        }

        "map underlying canvas on mapCanvas" in {
            val state = Initialised(Canvas.filled(20, 10, ' '))
            state.mapCanvas(_ => Canvas.filled(5, 5, ' ')) shouldBe Initialised(Canvas.filled(5, 5, ' '))
        }
    }

    "An output PaintState" must {
        val stateIn = Output(Queue("ah"), Initialised(Canvas.filled(1, 1, ' ')))
        val stateUn = Output(Queue("ah"), Uninitialised)
        "say it is initialised if the underlying state is" in {
            stateIn.isInitialised shouldBe true
            stateUn.isInitialised shouldBe false
        }

        "map underlying canvas on mapCanvas" in {
            val mapped = stateIn.mapCanvas(_ => Canvas.filled(5, 5, ' '))
            val expected = Output(Queue("ah"), Initialised(Canvas.filled(5, 5, ' ')))
            mapped shouldBe expected
        }

        "properly consume its output" in {
            val (output, state) = stateIn.consumeOutput
            output shouldBe Queue("ah")
            state shouldBe Initialised(Canvas.filled(1, 1, ' '))
        }
    }

    "An final PaintState" must {
        val state: PaintState[Char] = Final
        "say it is not initialised" in {
            state.isInitialised shouldBe false
        }

        "remain unchanged on canvas mapping" in {
            state.mapCanvas(_.drawPoint(Point(0, 0), ???)) shouldBe state
        }

        "initialise canvas on withCanvas" in {
            state.withCanvas(Canvas.filled(20, 10, ' ')) shouldBe Initialised(Canvas.filled(20, 10, ' '))
        }
    }
}
