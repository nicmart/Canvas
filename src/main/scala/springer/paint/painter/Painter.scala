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
final case class Painter[In] private (
    plugins: Map[String, List[PluginWithDescription[In]]]
) {
    /**
      * A transition between PaintStates
      */
    type StateTransition = PaintState[In] => PaintState[In]

    /**
      * Register a new plugin with the given symbol
      */
    def addPlugin(symbol: String, plugin: Plugin[In]): Painter[In] = {
        val pluginWithDesc = PluginWithDescription(plugin, plugin.description(symbol))
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
    def run(state: PaintState[In], input: String): PaintState[In] =
        parser.parse(input.split(" ").toList) match {
            case Success(transition, _) => transition(state)
            case Failure(msg) =>
                state.addOutput(msg)
        }

    private def commandParser: Parser[List[PluginWithDescription[In]]] = {
        first.map(plugins).label(commandNotAvailableString)
    }

    private def sameCommandParser(ps: List[PluginWithDescription[In]]): Parser[StateTransition] = {
        val descriptions = ps.map(_.description).mkString("\n")
        val parser = ps.foldLeft[Parser[StateTransition]](failing()) { (parser, pluginWithDesc) =>
            val pluginParser = pluginWithDesc.plugin.parser.label(descriptions)
            parser or pluginParser
        }
        parser.mapFailure { case Failure(msg) => Failure(commandMalformedString + "\n" + msg) }
    }

    private val commandNotAvailableString =
        "Command not available"

    private val commandMalformedString =
        "The command you entered was malformed.\nProbably you meant:"
}

object Painter {
    /**
      * Decorate a plugin with a description
      */
    case class PluginWithDescription[In](plugin: Plugin[In], description: String)

    /**
      * An empty (without plugins) painter
      */
    def empty[In]: Painter[In] = Painter(Map.empty)
}
