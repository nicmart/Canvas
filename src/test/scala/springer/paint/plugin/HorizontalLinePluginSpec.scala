package springer.paint.plugin

import springer.paint.terminal.{BaseParserSpec, Failure, ParserSpec, Success}
import HorizontalLinePlugin.HorizontalLine
import springer.paint.canvas.{CharCanvas, DrawPoint, DrawSequence}
import springer.paint.point.Point

class HorizontalLinePluginSpec extends BasePluginSpec {
    val plugin = HorizontalLinePlugin

    "The parser of the horizontal plugin" should {
        val parser = plugin.commandParser
        "parse valid horizontal line commands" in {
            val tokens = tokenize("0 0 10 0")
            val expectedCommand = HorizontalLine(0, 0, 10)
            parser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }
        "refuse non horizontal line commands" in {
            val tokens = tokenize("0 1 10 0")
            inside(parser.parse(tokens)) {
                case Failure(_) =>
            }
        }
        "refuse non-integers values" in {
            val tokens = tokenize("0 0 10 0x")
            inside(parser.parse(tokens)) {
                case Failure(_) =>
            }
        }

        "refuse other malformed commands" in {
            val tokens = tokenize("asd 20 30")
            inside(parser.parse(tokens)) {
                case Failure(_) =>
            }
        }
    }

    "The interpreter of an horizontal plugin" should {
        val interpreter = plugin.toCanvasDsl _
        val canvas = CharCanvas.empty(30, 10)
        "draw an horizontal line as a sequence of points" in {
            val command = HorizontalLine(1, 1, 2)
            val expected = DrawSequence(List(
                DrawPoint(Point(1, 1), 'x'),
                DrawPoint(Point(2, 1), 'x')
            ))
            interpreter(command) shouldBe expected
        }
    }
}
