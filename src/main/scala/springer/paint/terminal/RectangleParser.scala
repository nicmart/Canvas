package springer.paint.terminal

import springer.paint.dsl.{HorizontalLine, PaintDsl, Rectangle}
import springer.paint.point.Point
import springer.paint.terminal.CommandParser.{CommandParserResult, Failure, Success}
import CommandParser._

/**
  * Created by Nicolò Martini on 17/06/2017.
  */
object RectangleParser extends CommandParser[Rectangle] {
    /**
      * Transform a list of token into a command parser result
      */
    def parse(tokens: List[String]): CommandParserResult[Rectangle] = {
        val parsedInts = sequenceOf(int, 4).parse(tokens)
        parsedInts map { ints => Rectangle(Point(ints(0), ints(1)), Point(ints(2), ints(3))) }
    }
}
