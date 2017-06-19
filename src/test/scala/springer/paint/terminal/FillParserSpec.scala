package springer.paint.terminal

import springer.paint.dsl.Fill
import springer.paint.point.Point

/**
  * Created by Nicolò Martini <nicolo@martini.io>.
  */
class FillParserSpec extends CommonParserSpec {
    "A Fill parser" should {
        "parse a valid command" in {
            FillParser.parse(tokenize("10 10 c")) shouldBe Success(Fill(Point(10, 10), 'c'), Nil)
        }
        "refuse invalid commands" in {
            inside(FillParser.parse(tokenize("10 10 cc"))) {
                case Failure(_) =>
            }
            inside(FillParser.parse(tokenize("aa 10 c"))) {
                case Failure(_) =>
            }
        }
    }
}
