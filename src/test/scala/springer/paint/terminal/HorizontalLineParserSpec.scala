package springer.paint.terminal

import springer.paint.dsl.HorizontalLine

class HorizontalLineParserSpec extends CommonParserSpec {
    "An Horizontal Line Parser" should {
        "parse valid horizontal line commands" in {
            val tokens = tokenize("0 0 10 0")
            val expectedCommand = HorizontalLine(0, 0, 10)
            HorizontalLineParser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }
        "refuse non horizontal line commands" in {
            val tokens = tokenize("0 1 10 0")
            inside(HorizontalLineParser.parse(tokens)) {
                case Failure(_) =>
            }
        }
        "refuse non-integers values" in {
            val tokens = tokenize("0 0 10 0x")
            inside(HorizontalLineParser.parse(tokens)) {
                case Failure(_) =>
            }
        }

        "refuse other malformed commands" in {
            val tokens = tokenize("10 20 30")
            inside(HorizontalLineParser.parse(tokens)) {
                case Failure(_) =>
            }
        }
    }
}
