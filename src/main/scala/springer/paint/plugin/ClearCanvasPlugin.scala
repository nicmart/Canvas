package springer.paint.plugin
import springer.paint.canvas.Canvas
import springer.paint.parser.CommonParsers.successful
import springer.paint.parser.Parser
import springer.paint.plugin.QuitPlugin.Quit

case class ClearCanvasPlugin[In](emptySymbol: In) extends CanvasPlugin[In] {
    /**
      * Apply this command to the canvas
      */
    def transformCanvas[In2 >: In](
        command: ClearCanvasCommand.type ,
        canvas: Canvas[In2]
    ): Canvas[In2] = {
        Canvas.filled(canvas.width, canvas.height, emptySymbol)
    }

    object ClearCanvasCommand

    /**
      * The type of the new Command
      */
    type Command = ClearCanvasCommand.type

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[ClearCanvasCommand.type] =
        successful(ClearCanvasCommand).finalise()


    /**
      * Return some help about this command
      *
      * @param commandSymbol The symbol this plugin is registered to in the painter
      */
    def description(commandSymbol: String): String =
        s"Clear Canvas\nUsage: $commandSymbol"
}
