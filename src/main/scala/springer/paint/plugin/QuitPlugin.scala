package springer.paint.plugin

import springer.paint.state.Final
import springer.paint.terminal.Parser
import springer.paint.terminal.CommonParsers._

final case class QuitPlugin[In]() extends Plugin[In] {

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
    def interpret(command: Quit.type, state: State): State =
        Final

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Quit.type] =
        successful(Quit)
}
