package springer.paint.terminal

import springer.paint.dsl.{HorizontalLine, PaintDsl, TerminalDsl}
import springer.paint.terminal.CommandParser.{CommandParserResult, Failure, Success}

/**
  * Parse an horizontal line command
  */
object HorizontalLineParser extends CommandParser[HorizontalLine] {
    def parse(tokens: List[String]): CommandParserResult[HorizontalLine] = tokens match {
        case x1 :: y1 :: x2 :: y2 :: tail if y1 == y2 =>
            Success(HorizontalLine(y1.toInt, x1.toInt, x2.toInt), tail)
        case x1 :: y1 :: x2 :: y2 :: tail =>
            Failure("Line not valid")
        case _ =>
            Failure("An horizontal line should be a sequence of 4 integers")
    }
}
