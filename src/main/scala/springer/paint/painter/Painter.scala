package springer.paint.painter

import springer.paint.painter.Painter.PluginWithDescription
import springer.paint.plugin.Plugin
import springer.paint.state.PaintState
import springer.paint.parser.{Failure, Parser, Success}
import springer.paint.parser.CommonParsers._

/**
  * Put together the plugins and implement the final parser,
  * that translate the user input to a PaintState transition.
  *
  * This object cannot be created directly, but through the empty method
  * in the companion object. Plugins can then be added with the
  * `addPlugin` method.
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
        val pluginsForThisSymbol = pluginWithDesc :: plugins.getOrElse(symbol, Nil)
        val newPlugins = plugins.updated(symbol, pluginsForThisSymbol)
        Painter(newPlugins)
    }

    /**
      * Given the input and a state, it returns the new state
      */
    def run(state: PaintState[In], input: String): PaintState[In] =
        parser.parse(input.split(" +").toList) match {
            case Success(transition, _) => transition(state)
            case Failure(msg) =>
                state.addOutput(msg)
        }

    /**
      * An OR between all plugin parsers
      */
    private def parser: Parser[StateTransition] = {
        commandParser.andThen(sameCommandParser)
    }

    /**
      * A parser that consumes only the command symbol, and returns
      * the list of possible plugins
      */
    private def commandParser: Parser[List[PluginWithDescription[In]]] = {
        first.map(plugins).label(commandNotAvailableString)
    }

    /**
      * Given a list of plugins, return a parser that will parse the ramaining
      * part of the command and return the State Transition of the matching plugin
      */
    private def sameCommandParser(ps: List[PluginWithDescription[In]]): Parser[StateTransition] = {
        val descriptions = ps.map(_.description).mkString("\n")
        val parser = ps.foldLeft[Parser[StateTransition]](failing()) { (parser, pluginWithDesc) =>
            val pluginParser = pluginWithDesc.plugin.parser.label(descriptions)
            parser or pluginParser
        }
        parser.mapFailure { case Failure(msg) => Failure(commandMalformedString + "\n" + msg) }
    }

    /**
      * Failure string returned when a command is not recognised
      */
    private val commandNotAvailableString =
        "Command not available"

    /**
      * Failure string returned when a command is matched but the command
      * is malformed
      */
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
