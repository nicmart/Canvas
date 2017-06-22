package springer.paint.dsl.parser

import springer.paint.dsl.NewCanvas
import springer.paint.terminal.CommonParsers.{positiveInt, single}
import springer.paint.terminal.Parser.times
import springer.paint.terminal.{Parser, ParserResult}

/**
  * Parse a New Canvas command
  */
object NewCanvasParser extends Parser[NewCanvas] {
    def parse(tokens: List[String]): ParserResult[NewCanvas] = {
        val parser = times(positiveInt, 2).map(ints => NewCanvas(ints(0), ints(1)))
        single("C", parser).flatten().parse(tokens)
    }
}