package springer.paint.plugin

import springer.paint.state.{Final, PaintState}
import springer.paint.terminal.Parser
import springer.paint.terminal.CommonParsers._

/**
  * Quit plugin that implements the Quit command
  */
object QuitPlugin extends Plugin[Nothing] {

    object Quit

    /**
      * The type of the new Command
      */
    type Command = Quit.type

    /**
      * Return some help about this command
      *
      * @param commandSymbol The symbol this plugin is registered to in the painter
      */
    def description(commandSymbol: String): String =
        s"""
           |Quit command: exit
           |Format: $commandSymbol
         """.stripMargin.trim

    /**
      * Interpret the command
      */
    override def interpret[In2 >: Nothing](command: Quit.type, state: PaintState[In2]): PaintState[In2] =
        Final.addOutput("Bye!")

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Quit.type] =
        successful(Quit)
}
