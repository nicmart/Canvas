package springer.paint.spec

import org.scalatest.{FeatureSpec, GivenWhenThen, Inside, Matchers}
import springer.paint.canvas.CharCanvas
import springer.paint.dsl.{HorizontalLine, NewCanvas, PaintDsl}
import springer.paint.painter.{CanvasPainter, Painter}
import springer.paint.state.{Initialised, PaintState, Uninitialised}


/**
  * Created by Nicolò Martini on 27/04/2017.
  */
class PaintSpec extends FeatureSpec with GivenWhenThen with Matchers with Inside {

    import springer.paint.app.config.Wiring._

    feature("Initialising the canvas") {
        scenario("User creates a new Canvas") {

            Given("A painter")
            val painter: DefaultPainter = new CanvasPainter

            Given("An initial state")
            var currentState: DefaultPaintState = Uninitialised[Char, String]()

            When("the user creates a new canvas 20x10")
            currentState = painter(currentState, NewCanvas(20, 10))

            Then("I should see an empty canvas of the correct size")

            assertOutput(currentState, List.fill(10)(" " * 20).mkString("\n"))
        }

        scenario("User draws a new Canvas on an existing one") {

            Given("A painter")
            val painter: DefaultPainter = new CanvasPainter

            Given("An initial state")
            var currentState: DefaultPaintState = Uninitialised[Char, String]()

            Given("the user has created a new canvas 20x10")
            currentState = painter(currentState, NewCanvas(20, 10))

            When("the user creates a new canvas 10x10")
            currentState = painter(currentState, NewCanvas(10, 10))

            Then("I should see an empty canvas of the correct size")

            assertOutput(currentState, List.fill(10)(" " * 10).mkString("\n"))
        }

        scenario("A User draws an horizontal line") {
            Given("A painter")
            val painter: DefaultPainter = new CanvasPainter

            Given("An initial canvas")
            var currentState: DefaultPaintState = Initialised(CharCanvas.empty(4, 2))

            When("the user draws an horizontal line")
            currentState = painter(currentState, HorizontalLine(1, 0, 2))

            Then("I should see an horizontal Line")

            assertOutput(currentState, "    \nxxx ")
        }
    }

    def assertOutput(state: DefaultPaintState, output: String) = {
        inside(state) {
            case Initialised(canvas) =>
                assert(canvas.output == output)
        }
    }
}
