package springer.paint.terminal

import springer.paint.dsl.{HorizontalLine, PaintDsl, TerminalDsl}
import Parser._

/**
  * Parse an horizontal line command
  */
object HorizontalLineParser extends Parser[HorizontalLine] {
    def parse(tokens: List[String]): ParserResult[HorizontalLine] = {
        val parsedInts = times(int, 4).parse(tokens)
        parsedInts.mapSuccess { success =>
            success.value match {
                case x1 :: y1 :: x2 :: y2 :: tail if y1 == y2 =>
                    Success(HorizontalLine(y1, x1, x2), success.tail)
                case _ =>
                    Failure("Line not valid")
            }
        }
    }
}
