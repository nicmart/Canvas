package springer.paint.painter

import springer.paint.parser.{Failure, Parser, Success}
import springer.paint.plugin.{BasePluginSpec, Plugin, QuitPlugin}
import springer.paint.state.{Final, PaintState, Uninitialised}

class PainterSpec extends BasePluginSpec {
    "A Painter" should {
        val painter = Painter.empty[Char]
            .addPlugin("P", PluginStub("hello"))
            .addPlugin("P", PluginStub("world"))
        val initialState = Uninitialised

        "give the parser that returns the transition of the plugin that matches the input" in {
            val state = painter.run(initialState, "P world")
            state.consumeOutput._2 shouldBe Final
        }

        "fail if no plugin matches the input" in {
            val state = painter.run(initialState, "P missing")
            state.consumeOutput._2 shouldBe initialState
        }

        "fail if the command symbol is not recognized" in {
            val state = painter.run(initialState, "Q hello")
            state.consumeOutput._2 shouldBe initialState
        }
    }
}

private case class PluginStub(token: String) extends Plugin[Char] {
    import springer.paint.parser.CommonParsers._
    /**
      * The type of the new Command
      */
    type Command = String

    /**
      * Interpret the command
      */
    def interpret[In2 >: Char](command: String, state: PaintState[In2]): PaintState[In2] =
        Final

    /**
      * Parse an user input into this command
      */
    def commandParser: Parser[String] = matchFirst(token)

    /**
      * Return some help about this command
      *
      * @param commandSymbol The symbol this plugin is registered to in the painter
      */
    def description(commandSymbol: String): String = "Stub plugin"
}
