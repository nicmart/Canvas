package springer.paint.plugin

import springer.paint.canvas.{Canvas, DrawPoint, DrawSequence}
import springer.paint.point.Point
import springer.paint.parser.{BaseParserSpec, Failure, Success}

class VerticalLinePluginSpec extends BasePluginSpec {
    val plugin = VerticalLinePlugin('x')

    "The parser of the vertical line plugin" should {
        val parser = plugin.commandParser
        "parse valid vertical line commands" in {
            val tokens = tokenize("0 0 0 10")
            val expectedCommand = plugin.VerticalLine(0, 0, 10)
            parser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }

        "refuse non vertical line commands" in {
            val tokens = tokenize("0 0 10 0")
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
            val tokens = tokenize("10 20 30")
            inside(parser.parse(tokens)) {
                case Failure(_) =>
            }
        }
    }

    "The interpreter of an vertical plugin" should {
        val interpreter = plugin.toCanvasDsl _
        val canvas = Canvas.filled(30, 10, ' ')
        "draw an vertical line as a sequence of points" in {
            val command = plugin.VerticalLine(1, 1, 2)
            val expected = DrawSequence(List(
                DrawPoint(Point(1, 1), 'x'),
                DrawPoint(Point(1, 2), 'x')
            ))
            interpreter(command) shouldBe expected
        }
    }
}
