package springer.paint.plugin

import springer.paint.plugin.FillPlugin.Fill
import springer.paint.point.Point
import springer.paint.spec.CommonSpec
import springer.paint.terminal.{BaseParserSpec, Failure, ParserSpec, Success}

class FillPluginSpec extends BaseParserSpec {
    val plugin = FillPlugin
    "The parser of the fill plugin" should {
        val parser = plugin.commandParser
        "parse valid horizontal line commands" in {
            val tokens = tokenize("B 0 0 x")
            val expectedCommand = Fill(Point(0, 0), 'x')
            parser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }

        "refuse non-integers values" in {
            val tokens = tokenize("B 0 10 0x")
            inside(parser.parse(tokens)) {
                case Failure(_) =>
            }
        }

        "refuse other malformed commands" in {
            val tokens = tokenize("10 20 30")
            inside(parser.parse(tokens)) {
                case Failure(_) =>
            }
        }
    }
}
