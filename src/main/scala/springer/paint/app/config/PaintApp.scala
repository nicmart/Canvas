package springer.paint.app.config

import scala.annotation.tailrec
import scala.io.StdIn

object PaintApp extends App {

    import Wiring._

    loop(initialState)

    @tailrec
    def loop(state: DefaultPaintState): Unit = {
        val line = StdIn.readLine("Enter command: ")
        val (output, nextState) = painter.run(state, line).consumeOutput
        if (output.isEmpty) {
            nextState.mapCanvas(c => { println(c.output); c})
        } else {
            output.foreach(println)
        }
        if (!nextState.isFinal) {
            loop(nextState)
        }
    }
}
