package springer.paint.terminal

import springer.paint.dsl.VerticalLine
import springer.paint.terminal.CommandParser.{Failure, Success}

class VerticalLineParserSpec extends CommonParserSpec {
    "An Vertical Line Parser" should {
        "parse valid vertical line commands" in {
            val tokens = tokenize("0 0 0 10")
            val expectedCommand = VerticalLine(0, 0, 10)
            VerticalLineParser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }
        "refuse non vertical line commands" in {
            val tokens = tokenize("1 0 10 0")
            inside(VerticalLineParser.parse(tokens)) {
                case Failure(_) =>
            }
        }
        "refuse non-integers values" in {
            val tokens = tokenize("0 0 0 10x")
            inside(HorizontalLineParser.parse(tokens)) {
                case Failure(_) =>
            }
        }
        "refuse other malformed commands" in {
            val tokens = tokenize("10 20 30")
            inside(VerticalLineParser.parse(tokens)) {
                case Failure(_) =>
            }
        }
    }
}
