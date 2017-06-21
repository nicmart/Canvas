package springer.paint.terminal

import springer.paint.dsl.NewCanvasLegacy
import Parser._
import CommonParsers._

/**
  * Created by Nicolò Martini on 17/06/2017.
  */
object NewCanvasParser extends Parser[NewCanvasLegacy] {
    def parse(tokens: List[String]): ParserResult[NewCanvasLegacy] = {
        times(positiveInt, 2).parse(tokens).map(ints => NewCanvasLegacy(ints(0), ints(1)))
    }
}
