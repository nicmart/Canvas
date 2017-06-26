package springer.paint.plugin

import springer.paint.canvas.Canvas
import springer.paint.point.Point
import springer.paint.parser.{BaseParserSpec, Failure, ParserSpec, Success}

class RectanglePluginSpec extends BasePluginSpec {
    val plugin = RectanglePlugin(HorizontalLinePlugin('x'), VerticalLinePlugin('x'))
    "The parser of a rectangle plugin" should {
        val parser = plugin.commandParser
        "parse a valid rectangle" in {
            val tokens = tokenize("0 0 10 5")
            val expected = plugin.Rectangle(Point(0, 0), Point(10, 5))
            parser.parse(tokens) shouldBe Success(expected, Nil)
        }

        "refuse malformed commands" in {
            inside(parser.parse(tokenize("10 20 3"))) {
                case Failure(_) =>
            }
            inside(parser.parse(tokenize("aaa 10 20 10"))) {
                case Failure(_) =>
            }
        }
    }

    "The interpreter of an rectangle plugin" should {
        val canvas = Canvas.filled(30, 10, ' ')
        "draw a rectangle as a sequence of points" in {
            val command = plugin.Rectangle(Point(0, 0), Point(1, 1))
            val expected = canvas
                .drawPoint(Point(0, 0), 'x')
                .drawPoint(Point(1, 0), 'x')
                .drawPoint(Point(1, 1), 'x')
                .drawPoint(Point(0, 1), 'x')

            plugin.transformCanvas(command, canvas) shouldBe expected
        }

        "draw a rectangle even if it is upside down" in {
            val command = plugin.Rectangle(Point(1, 1), Point(0, 0))
            val expected = canvas
                .drawPoint(Point(0, 0), 'x')
                .drawPoint(Point(1, 0), 'x')
                .drawPoint(Point(1, 1), 'x')
                .drawPoint(Point(0, 1), 'x')

            plugin.transformCanvas(command, canvas) shouldBe expected
        }

        "draw a degenerate rectangle as a single point" in {
            val command = plugin.Rectangle(Point(1, 1), Point(1, 1))
            val expected = canvas
                  .drawPoint(Point(1, 1), 'x')
            plugin.transformCanvas(command, canvas) shouldBe expected
        }
    }
}
