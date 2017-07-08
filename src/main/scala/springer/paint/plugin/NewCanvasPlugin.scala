package springer.paint.plugin

import springer.paint.canvas.Canvas
import springer.paint.state.{Initialised, PaintState}
import springer.paint.parser.CommonParsers.positiveInt
import springer.paint.parser.Parser
import springer.paint.parser.Parser._

/**
  * A plugin for the new Canvas command
  *
  * @param emptySymbol The symbol the new canvas will be filled with
  * @tparam In
  */
case class NewCanvasPlugin[In](emptySymbol: In) extends Plugin[In] {

    final case class NewCanvas(width: Int, height: Int)

    /**
      * The type of the new Command
      */
    type Command = NewCanvas

    /**
      * Return some help about this command
      *
      * @param commandSymbol The symbol this plugin is registered to in the painter
      */
    def description(commandSymbol: String): String =
        s"""
           |New Canvas command: create a new Canvas
           |Format: $commandSymbol w h, where w and h are positive integers
         """.stripMargin.trim

    /**
      * Interpret the command
      */
    override def interpret[In2 >: In](command: NewCanvas, state: PaintState[In2]): PaintState[In2] = {
        val newState = Initialised(Canvas.filled(command.width, command.height, emptySymbol))
        state.next(newState)
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[NewCanvas] = {
        combine(positiveInt, positiveInt)(NewCanvas).finalise()
    }
}
