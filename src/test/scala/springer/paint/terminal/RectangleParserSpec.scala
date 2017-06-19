package springer.paint.terminal

import springer.paint.dsl.Rectangle
import springer.paint.point.Point

/**
  * Created by Nicolò Martini <nicolo@martini.io>.
  */
class RectangleParserSpec extends CommonParserSpec {
    "A Rectangle Parser" should {
        "parse a valid rectangle" in {
            val tokens = tokenize("0 0 10 5")
            val expected = Rectangle(Point(0, 0), Point(10, 5))
            RectangleParser.parse(tokens) shouldBe Success(expected, Nil)
        }
    }

    "A Rectangle Parser" should {
        "refuse malformed commands" in {
            inside(RectangleParser.parse(tokenize("10 20 3"))) {
                case Failure(_) =>
            }
            inside(RectangleParser.parse(tokenize("aaa 10 20 10"))) {
                case Failure(_) =>
            }
        }
    }
}
