package springer.paint.terminal

import springer.paint.dsl.VerticalLine
import springer.paint.terminal.Parser._
import CommonParsers._

/**
  * Parse a vertical line command
  */
object VerticalLineParser extends Parser[VerticalLine] {
    def parse(tokens: List[String]): ParserResult[VerticalLine] = {
        val parsedInts = times(int, 4).parse(tokens)
        parsedInts.mapSuccess { success =>
            success.value match {
                case x1 :: y1 :: x2 :: y2 :: tail if x1 == x2 =>
                    Success(VerticalLine(x1, y1, y2), success.tail)
                case _ =>
                    Failure("Line not valid")
            }
        }
    }
}
