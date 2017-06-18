package springer.paint.terminal

import springer.paint.dsl.{HorizontalLine, PaintDsl, TerminalDsl, VerticalLine}
import springer.paint.terminal.CommandParser._

/**
  * Parse a vertical line command
  */
object VerticalLineParser extends CommandParser[VerticalLine] {
    def parse(tokens: List[String]): CommandParserResult[VerticalLine] = {
        val parsedInts = sequenceOf(int, 4).parse(tokens)
        parsedInts.mapSuccess { success =>
            success.value match {
                case x1 :: y1 :: x2 :: y2 :: tail if x1 == x2 =>
                    Success(VerticalLine(x1, y1, y2), success.tail)
                case x1 :: y1 :: x2 :: y2 :: tail =>
                    Failure("Line not valid")
                case _ =>
                    Failure("An horizontal line should be a sequence of 4 integers")
            }
        }
    }
}
