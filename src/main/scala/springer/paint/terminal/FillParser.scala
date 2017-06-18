package springer.paint.terminal

import springer.paint.dsl.Fill
import springer.paint.point.Point
import springer.paint.terminal.CommandParser.{CommandParserResult, Failure, Success}
import CommandParser._

/**
  * Created by Nicolò Martini
  */
object FillParser extends CommandParser[Fill]{
    /**
      * Transform a list of token into a command parser result
      */
    def parse(tokens: List[String]): CommandParserResult[Fill] = {
        val pointParser = combine(int, int)(Point)
        combine(pointParser, char)(Fill).parse(tokens)
    }
}
