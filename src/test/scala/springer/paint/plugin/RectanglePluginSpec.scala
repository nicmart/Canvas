package springer.paint.plugin

import RectanglePlugin.Rectangle
import springer.paint.canvas.{CharCanvas, DrawPoint, DrawSequence}
import springer.paint.point.Point
import springer.paint.terminal.{BaseParserSpec, Failure, ParserSpec, Success}
import DrawSequence._

class RectanglePluginSpec extends BaseParserSpec {
    val plugin = RectanglePlugin
    "The parser of a rectangle plugin" should {
        val parser = plugin.commandParser
        "parse a valid rectangle" in {
            val tokens = tokenize("R 0 0 10 5")
            val expected = Rectangle(Point(0, 0), Point(10, 5))
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
        val interpreter = plugin.toCanvasDsl _
        val canvas = CharCanvas.empty(30, 10)
        "draw a rectangle as a sequence of points" in {
            val command = Rectangle(Point(0, 0), Point(1, 1))
            val expected = DrawSequence(List(
                DrawPoint(Point(0, 0), 'x'),
                DrawPoint(Point(1, 0), 'x'),
                DrawPoint(Point(1, 1), 'x'),
                DrawPoint(Point(0, 1), 'x')
            ))
            flatten(interpreter(command)) shouldBe expected
        }

        "draw a rectangle even if it is upside down" in {
            val command = Rectangle(Point(1, 1), Point(0, 0))
            val expected = DrawSequence(List(
                DrawPoint(Point(0, 0), 'x'),
                DrawPoint(Point(1, 0), 'x'),
                DrawPoint(Point(1, 1), 'x'),
                DrawPoint(Point(0, 1), 'x')
            ))
            flatten(interpreter(command)) shouldBe expected
        }

        "draw a degenerate rectangle as a single point" in {
            val command = Rectangle(Point(0, 0), Point(0, 0))
            val expected = DrawPoint(Point(0, 0), 'x')
            flatten(interpreter(command)) shouldBe expected
        }
    }
}
