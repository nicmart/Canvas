package springer.paint.plugin

import springer.paint.state.Final
import springer.paint.terminal.Parser
import springer.paint.terminal.CommonParsers._

final case class QuitPlugin[In, Out]() extends Plugin[In, Out] {

    object Quit

    /**
      * The type of the new Command
      */
    type Command = Quit.type

    /**
      * Interpret the command
      */
    def interpret(command: Quit.type, state: State): State =
        Final()

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[Quit.type] =
        single("Q", Quit)
}
