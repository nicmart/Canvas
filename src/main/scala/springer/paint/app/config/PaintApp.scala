package springer.paint.app.config

import springer.paint.dsl.GetCommand
import springer.paint.state.PaintState
import springer.paint.terminal.DefaultStringParser

import scala.io.StdIn

/**
  * Created by Nicolò Martini on 29/04/2017.
  */
object PaintApp extends App {

    var state: Wiring.DefaultPaintState = Wiring.initialState
    val painter = Wiring.painter
    val parser = DefaultStringParser

    while (true) {
        val line = StdIn.readLine("Enter command: ")
        parser.parse(line) match {
            case Some(command) =>
                state = painter(state, command)
                state.mapCanvas { canvas =>
                    println(canvas.output)
                    canvas
                }
            case None => println("Unrecognized command")
        }
    }
}
