package springer.paint.plugin
import springer.paint.parser.CommonParsers.successful
import springer.paint.parser.Parser
import springer.paint.plugin.QuitPlugin.Quit
import springer.paint.state.{Initialised, PaintState}

case class UndoPlugin[In]() extends Plugin[In] {

    object Undo

    /**
      * The type of the new Command
      */
    type Command = Undo.type

    /**
      * Interpret the command
      */
    def interpret[In2 >: In](command: Command, state: PaintState[In2]): PaintState[In2] = {
        state match {
            case Initialised(canvas, last :: tail) => last
            case _ => state
        }
    }

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Command] =
        successful(Undo).finalise()

    /**
      * Return some help about this command
      *
      * @param commandSymbol The symbol this plugin is registered to in the painter
      */
    def description(commandSymbol: String): String = "Undo command"
}
