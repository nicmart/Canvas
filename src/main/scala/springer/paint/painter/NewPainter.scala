package springer.paint.painter

import springer.paint.dsl.{PaintDsl, PaintDslInterpreter}
import springer.paint.dsl.parser.NewCanvasParser
import springer.paint.plugin.Plugin
import springer.paint.state.PaintState
import springer.paint.terminal.{Parser, Success}

final case class NewPainter[In, Out](
    interpreter: PaintDslInterpreter[In, Out],
    plugins: List[Plugin[In]]
) {
    /**
      * An OR between all plugin parsers
      */
    val parser =
        plugins.foldLeft(NewCanvasParser: Parser[PaintDsl[In]]){
            (parser, plugin) => parser or plugin.parser
        }

    def run(state: PaintState[In, Out], input: String): PaintState[In, Out] =
        parser.parse(input.split(" ").toList) match {
            case Success(command, _) => interpreter.run(state, command)
            case _ => state
        }
}
