package springer.paint.terminal

import springer.paint.dsl.{HorizontalLine, PaintDsl, TerminalDsl, VerticalLine}
import springer.paint.terminal.CommandParser.{CommandParserResult, Failure, Success}

/**
  * Parse a vertical line command
  */
object VerticalLineParser extends CommandParser[VerticalLine] {
    def parse(tokens: List[String]): CommandParserResult[VerticalLine] = tokens match {
        case x1 :: y1 :: x2 :: y2 :: tail if x1 == x2 =>
            Success(VerticalLine(x1.toInt, y1.toInt, y2.toInt), tail)
        case x1 :: y1 :: x2 :: y2 :: tail =>
            Failure("Line not valid")
        case _ =>
            Failure("An horizontal line should be a sequence of 4 integers")
    }
}
