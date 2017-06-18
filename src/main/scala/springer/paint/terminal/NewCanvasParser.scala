package springer.paint.terminal

import springer.paint.dsl.NewCanvas
import springer.paint.terminal.CommandParser.{CommandParserResult, Failure, Success}
import CommandParser._

/**
  * Created by Nicolò Martini on 17/06/2017.
  */
object NewCanvasParser extends CommandParser[NewCanvas] {
    def parse(tokens: List[String]): CommandParserResult[NewCanvas] = {
        sequenceOf(positiveInt, 2).parse(tokens).map(ints => NewCanvas(ints(0), ints(1)))
    }
}
