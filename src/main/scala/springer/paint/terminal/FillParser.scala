package springer.paint.terminal

import springer.paint.dsl.Fill
import springer.paint.point.Point
import Parser._
import CommonParsers._

/**
  * Created by Nicolò Martini
  */
object FillParser extends Parser[Fill]{
    /**
      * Transform a list of token into a command parser result
      */
    def parse(tokens: List[String]): ParserResult[Fill] = {
        val pointParser = combine(int, int)(Point)
        combine(pointParser, char)(Fill).parse(tokens)
    }
}
