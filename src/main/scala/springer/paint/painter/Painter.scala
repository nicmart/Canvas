package springer.paint.painter

import springer.paint.plugin.Plugin
import springer.paint.state.PaintState
import springer.paint.terminal.{Failure, Parser, Success}
import springer.paint.terminal.CommonParsers.failing

final case class Painter[In, Out](
    plugins: List[Plugin[In, Out]]
) {
    type StateTransition = PaintState[In, Out] => PaintState[In, Out]
    /**
      * An OR between all plugin parsers
      */
    def parser: Parser[StateTransition] =
        plugins.foldLeft[Parser[StateTransition]](failing("No plugins registered")) {
            (parser, plugin) => parser or plugin.parser
        }

    def run(state: PaintState[In, Out], input: String): PaintState[In, Out] =
        parser.parse(input.split(" ").toList) match {
            case Success(transition, _) => transition(state)
            case Failure(msg) => state
        }
}
