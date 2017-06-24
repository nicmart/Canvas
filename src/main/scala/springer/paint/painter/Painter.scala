package springer.paint.painter

import springer.paint.painter.Painter.PluginWithDescription
import springer.paint.plugin.Plugin
import springer.paint.state.PaintState
import springer.paint.terminal.{Failure, Parser, Success}
import springer.paint.terminal.CommonParsers._

/**
  * Put together the plugins and implement the final parser.
  * This object cannot be created directly, but through the empty method
  * in the companion object.
  */
final case class Painter[In, Out] private (
    plugins: Map[String, List[PluginWithDescription[In, Out]]]
) {
    type StateTransition = PaintState[In, Out] => PaintState[In, Out]

    /**
      * Register a new plugin with the given symbol
      */
    def addPlugin(symbol: String, plugin: Plugin[In, Out], desc: String): Painter[In, Out] = {
        val pluginWithDesc = PluginWithDescription(plugin, desc)
        val newPlugins = plugins.updated(symbol, pluginWithDesc :: plugins.getOrElse(symbol, Nil))
        Painter(newPlugins)
    }

    /**
      * An OR between all plugin parsers
      */
    def parser: Parser[StateTransition] = {
        commandParser.andThen(sameCommandParser)
    }

    /**
      * Parse the input and update the state
      */
    def run(state: PaintState[In, Out], input: String): PaintState[In, Out] =
        parser.parse(input.split(" ").toList) match {
            case Success(transition, _) => transition(state)
            case Failure(msg) =>
                // @todo remove this
                println("Invalid Command")
                println(msg)
                state
        }

    private def commandParser: Parser[List[PluginWithDescription[In, Out]]] = {
        first.map(plugins).label(commandNotAvailableString)
    }

    private def sameCommandParser(ps: List[PluginWithDescription[In, Out]]): Parser[StateTransition] = {
        val descriptions = ps.map(_.description).mkString("\n")
        ps.foldLeft[Parser[StateTransition]](failing()) { (parser, pluginWithDesc) =>
            val pluginParser = pluginWithDesc.plugin.parser.label(descriptions)
            parser or pluginParser
        }
    }

    private val commandNotAvailableString =
        "Command not available"
}

object Painter {
    /**
      * Decorate a plugin with a description
      */
    case class PluginWithDescription[In, Out](plugin: Plugin[In, Out], description: String)

    /**
      * An empty (without plugins) painter
      */
    def empty[In, Out]: Painter[In, Out] = Painter(Map.empty)
}
