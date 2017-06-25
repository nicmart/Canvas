package springer.paint.plugin

import springer.paint.canvas.Canvas
import springer.paint.state.{Initialised, PaintState}
import springer.paint.terminal.CommonParsers.{positiveInt, single}
import springer.paint.terminal.Parser
import springer.paint.terminal.Parser._

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
           |New Canvas command: draw an horizontal line
           |Format: $commandSymbol w h, where w and h are positive integers
         """.stripMargin.trim

    /**
      * Interpret the command
      */
    def interpret(command: NewCanvas, state: PaintState[In]): PaintState[In] =
        Initialised(Canvas.filled(command.width, command.height, emptySymbol))

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[NewCanvas] = {
        combine(positiveInt, positiveInt)(NewCanvas)
    }
}
