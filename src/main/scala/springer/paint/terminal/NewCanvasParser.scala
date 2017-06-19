package springer.paint.terminal

import springer.paint.dsl.NewCanvas
import Parser._
import CommonParsers._

/**
  * Created by Nicolò Martini on 17/06/2017.
  */
object NewCanvasParser extends Parser[NewCanvas] {
    def parse(tokens: List[String]): ParserResult[NewCanvas] = {
        times(positiveInt, 2).parse(tokens).map(ints => NewCanvas(ints(0), ints(1)))
    }
}
