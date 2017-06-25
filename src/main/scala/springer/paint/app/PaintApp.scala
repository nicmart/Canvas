package springer.paint.app

import springer.paint.canvas.CanvasRenderer
import springer.paint.painter.Painter
import springer.paint.state.PaintState
import scala.annotation.tailrec
import scala.io.StdIn

/**
  * The main application loop
  */
final case class PaintApp(
    initialState: PaintState[Char],
    painter: Painter[Char],
    renderer: CanvasRenderer[Char, String]
) {
    /**
      * Start the application
      */
    def start(): Unit =
        loop(initialState)


    /**
      * The main application loop
      * It prints the state output, and if the state is not final,
      * it asks the user input for a new command.
      * 
      * It exits otherwise.
      */
    @tailrec private def loop(state: PaintState[Char]): Unit = {

        val stateWithoutOutput = printOutput(state)

        if (!stateWithoutOutput.isFinal) {
            val line = StdIn.readLine("Enter command: ")
            loop(painter.run(stateWithoutOutput, line))
        }
    }

    /**
      * Consume the output, and print the canvas if necessary
      * Return then the state without output
      */
    private def printOutput(state: PaintState[Char]): PaintState[Char] = {
        val (output, stateWithoutOutput) = state.consumeOutput
        if (output.isEmpty) {
            stateWithoutOutput.mapCanvas(canvas => {
                println(renderer.render(canvas))
                canvas
            })
        } else {
            output.foreach(println)
        }
        stateWithoutOutput
    }
}
