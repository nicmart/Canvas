package springer.paint.spec

import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}
import springer.paint.dsl.{NewCanvas, PaintDsl}
import springer.paint.painter.{Painter, StringPainter}
import springer.paint.state.{PaintState, StringPaintState}


/**
  * Created by Nicolò Martini on 27/04/2017.
  */
class PaintSpec extends FeatureSpec with GivenWhenThen with Matchers {

    feature("Initialising the canvas") {
        scenario("User creates a new Canvas") {

            Given("A painter")
            val painter: Painter[String] = new StringPainter

            Given("An initial state")
            var currentState = StringPaintState.empty

            When("the user creates a new canvas 20x10")
            currentState = painter(currentState, NewCanvas(20, 10))

            Then("I should see an empty canvas of the correct size")
            assert(
                currentState.output == List.fill(10)(" " * 20).mkString("\n")
            )
        }

        scenario("User draws a new Canvas on an existing one") {

            Given("A painter")
            val painter: Painter[String] = new StringPainter

            Given("An initial state")
            var currentState = StringPaintState.empty

            Given("the user created a new canvas 20x10")
            currentState = painter(currentState, NewCanvas(20, 10))

            When("the user creates a new canvas 10x10")
            currentState = painter(currentState, NewCanvas(10, 10))

            Then("I should see an empty canvas of the correct size")
            assert(
                currentState.output == List.fill(10)(" " * 10).mkString("\n")
            )
        }

        scenario("User draws an horizontal line") {
            Given("A painter")
            val painter: Painter[String] = new StringPainter

            Given("An initial state")
            var currentState = StringPaintState.empty

        }
    }



}
