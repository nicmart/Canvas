package springer.paint.terminal

import springer.paint.dsl.{HorizontalLine, PaintDsl, TerminalDsl}
import springer.paint.terminal.CommandParser.{CommandParserResult, Failure, Success}
import CommandParser._

/**
  * Parse an horizontal line command
  */
object HorizontalLineParser extends CommandParser[HorizontalLine] {
    def parse(tokens: List[String]): CommandParserResult[HorizontalLine] = {
        val parsedInts = sequenceOf(int, 4).parse(tokens)
        parsedInts.mapSuccess { success =>
            success.value match {
                case x1 :: y1 :: x2 :: y2 :: tail if y1 == y2 =>
                    Success(HorizontalLine(y1.toInt, x1.toInt, x2.toInt), success.tail)
                case x1 :: y1 :: x2 :: y2 :: tail =>
                    Failure("Line not valid")
                case _ =>
                    Failure("An horizontal line should be a sequence of 4 integers")
            }
        }
    }
}
