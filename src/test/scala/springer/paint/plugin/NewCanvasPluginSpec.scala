package springer.paint.plugin

import springer.paint.plugin.FillPlugin.Fill
import springer.paint.plugin.NewCanvasPlugin.NewCanvas
import springer.paint.point.Point
import springer.paint.terminal.{BaseParserSpec, Failure, ParserSpec, Success}

class NewCanvasPluginSpec extends BaseParserSpec {
    val plugin = NewCanvasPlugin
    "The parser of the new canvas plugin" should {
        val parser = plugin.commandParser
        "parse valid commands" in {
            val tokens = tokenize("C 10 1")
            val expectedCommand = NewCanvas(10, 1)
            parser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }

        "refuse non-positive widths" in {
            val tokens = tokenize("C 0 10")
            inside(parser.parse(tokens)) {
                case Failure(_) =>
            }
        }

        "refuse non-positive heights" in {
            val tokens = tokenize("C 10 0")
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
