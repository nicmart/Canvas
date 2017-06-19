package springer.paint.terminal

import springer.paint.dsl.Rectangle
import springer.paint.point.Point
import Parser._

/**
  * Created by Nicolò Martini on 17/06/2017.
  */
object RectangleParser extends Parser[Rectangle] {
    /**
      * Transform a list of token into a command parser result
      */
    def parse(tokens: List[String]): ParserResult[Rectangle] = {
        val parsedInts = times(int, 4).parse(tokens)
        parsedInts map { ints => Rectangle(Point(ints(0), ints(1)), Point(ints(2), ints(3))) }
    }
}
