package springer.paint.app.config

import springer.paint.terminal._

import scala.io.StdIn

/**
  * Created by Nicolò Martini on 29/04/2017.
  */
object PaintApp extends App {

    var state: Wiring.DefaultPaintState = Wiring.initialState
    val painter = Wiring.painter
    val parser = Wiring.parser

    while (true) {
        val line = StdIn.readLine("Enter command: ")
        state = painter.run(state, line)
        state.mapCanvas(c => { println(c.output); c})
    }
}
