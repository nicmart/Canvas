package springer.paint.plugin

import springer.paint.canvas.Canvas
import springer.paint.plugin.FillPlugin.Fill
import springer.paint.point.Point
import springer.paint.terminal.{BaseParserSpec, Failure, Success}

class FillPluginSpec extends BasePluginSpec {
    val plugin = FillPlugin
    "The parser of the fill plugin" should {
        val parser = plugin.commandParser
        "parse valid horizontal line commands" in {
            val tokens = tokenize("0 0 x")
            val expectedCommand = Fill(Point(0, 0), 'x')
            parser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }

        "refuse non-integers values" in {
            val tokens = tokenize("0 10 0x")
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

    "The interpreter of a Fill Plugin" should {
        val transition = plugin.transformCanvas _
        val canvas = Canvas.filled(10, 2, ' ')
            .drawPoint(Point(8, 1), 'x')
            .drawPoint(Point(8, 2), 'x')
        "Fill contiguous blocks of pixels" in {
            val newCanvas = transition(Fill(Point(9, 1), 'o'), canvas)
            newCanvas.valueAt(Point(9, 1)) shouldBe Some('o')
            newCanvas.valueAt(Point(9, 2)) shouldBe Some('o')
            newCanvas.valueAt(Point(10, 1)) shouldBe Some('o')
            newCanvas.valueAt(Point(10, 2)) shouldBe Some('o')
            newCanvas.valueAt(Point(8, 1)) shouldBe Some('x')
            newCanvas.valueAt(Point(1, 1)) shouldBe Some(' ')
        }
    }
}
