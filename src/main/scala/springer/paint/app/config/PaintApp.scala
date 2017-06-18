package springer.paint.app.config

import springer.paint.terminal.CommandParser.{Failure, Success}

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
        val tokens = line.split(" ").toList
        parser.parse(tokens) match {
            case Success(command, Nil) =>
                state = painter(state, command)
                state.mapCanvas { canvas =>
                    println(canvas.output)
                    canvas
                }
            case Success(command, _) => println("Too many characters in the command!")
            case Failure(error) => println(error)
        }
    }
}
