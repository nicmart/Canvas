package springer.paint.spec

import org.scalatest.{FeatureSpec, GivenWhenThen, Inside, Matchers}
import springer.paint.canvas.CharCanvas
import springer.paint.dsl._
import springer.paint.painter.{CanvasPainter, NewPainter}
import springer.paint.point.Point
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
            currentState = painter(currentState, NewCanvasLegacy(20, 10))

            Then("I should see an empty canvas of the correct size")

            assertOutput(currentState, List.fill(10)(" " * 20).mkString("\n"))
        }

        scenario("User draws a new Canvas on an existing one") {

            Given("A painter")
            val painter: DefaultPainter = new CanvasPainter

            Given("An initial state")
            var currentState: DefaultPaintState = Uninitialised[Char, String]()

            Given("the user has created a new canvas 20x10")
            currentState = painter(currentState, NewCanvasLegacy(20, 10))

            When("the user creates a new canvas 10x10")
            currentState = painter(currentState, NewCanvasLegacy(10, 10))

            Then("I should see an empty canvas of the correct size")

            assertOutput(currentState, List.fill(10)(" " * 10).mkString("\n"))
        }

        scenario("A User draws an horizontal line") {
            Given("A painter")
            val painter: DefaultPainter = new CanvasPainter

            Given("An initial canvas")
            var currentState: DefaultPaintState = Initialised(CharCanvas.empty(4, 2))

            When("the user draws an horizontal line")
            currentState = painter(currentState, HorizontalLine(2, 1, 3))

            Then("I should see an horizontal Line")

            assertOutput(currentState, "    \nxxx ")
        }

        scenario("A User draws a vertical line") {
            Given("A painter")
            val painter: DefaultPainter = new CanvasPainter

            Given("An initial canvas")
            var currentState: DefaultPaintState = Initialised(CharCanvas.empty(2, 4))

            When("the user draws an horizontal line")
            currentState = painter(currentState, VerticalLine(2, 1, 3))

            Then("I should see an horizontal Line")

            assertOutput(currentState, " x\n x\n x\n  ")
        }

        scenario("A User draws a rectangle") {
            Given("A painter")
            val painter: DefaultPainter = new CanvasPainter

            Given("An initial canvas")
            var currentState: DefaultPaintState = Initialised(CharCanvas.empty(5, 5))

            When("the user draws a rectangle")
            currentState = painter(currentState, Rectangle(Point(2, 2), Point(4, 4)))

            Then("I should see a rectangle")

            assertOutput(currentState, "     \n xxx \n x x \n xxx \n     ")
        }

        scenario("A User bucket-fills starting from a point") {
            Given("A painter")
            val painter: DefaultPainter = new CanvasPainter

            Given("An initial canvas")
            var currentState: DefaultPaintState = Initialised(CharCanvas.empty(5, 5))

            When("the user draws a line")
            currentState = painter(currentState, VerticalLine(3, 1, 5))

            When("the users bucket-fill from 0, 0")
            currentState = painter(currentState, Fill(Point(1, 1), 'o'))

            assertOutput(currentState, "oox  \noox  \noox  \noox  \noox  ")
        }
    }

    def assertOutput(state: DefaultPaintState, output: String) = {
        inside(state) {
            case Initialised(canvas) =>
                assert(canvas.output == output)
        }
    }
}
