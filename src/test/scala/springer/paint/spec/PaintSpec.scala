package springer.paint.spec

import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}


/**
  * Created by Nicolò Martini on 27/04/2017.
  */
class PaintSpec extends FeatureSpec with GivenWhenThen with Matchers {

    feature("Initialising the canvas") {
        scenario("User creates a new Canvas") {

            Given("A painter")
            val painter: (PaintState, PaintCommand) => PaintState

            Given("An initial state")
            var currentState = PaintState.empty

            When("the user create a new canvas 20x10")
            currentState = painter(currentState, Canvas(20, 10))

            Then("I should see an empty canvas")
            assert(
                currentState.output == List.fill(20)(" " * 10).mkString("\n")
            )
        }
    }

}
