package springer.paint.plugin

import springer.paint.canvas.CharCanvas
import springer.paint.plugin.FillPlugin.Fill
import springer.paint.plugin.NewCanvasPlugin.NewCanvas
import springer.paint.point.Point
import springer.paint.state.{Initialised, Uninitialised}
import springer.paint.terminal.{BaseParserSpec, Failure, ParserSpec, Success}

class NewCanvasPluginSpec extends BasePluginSpec {
    val plugin = NewCanvasPlugin
    "The parser of the new canvas plugin" should {
        val parser = plugin.commandParser
        "parse valid commands" in {
            val tokens = tokenize("10 1")
            val expectedCommand = NewCanvas(10, 1)
            parser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }

        "refuse non-positive widths" in {
            val tokens = tokenize("0 10")
            inside(parser.parse(tokens)) {
                case Failure(_) =>
            }
        }

        "refuse non-positive heights" in {
            val tokens = tokenize("10 0")
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

    "The interpreter of a new Canvas Plugin" should {
        val interpreter = plugin.interpret _
        val command = NewCanvas(30, 10)
        "initialise an un-initialised state" in {
            val start: NewCanvasPlugin.State = Uninitialised()
            val expected = Initialised(CharCanvas.empty(30, 10))
            interpreter(command, start) shouldBe expected
        }
        "replace an initialised state" in {
            val start: NewCanvasPlugin.State = Initialised(CharCanvas.empty(10, 10))
            val expected = Initialised(CharCanvas.empty(30, 10))
            interpreter(command, start) shouldBe expected
        }
    }
}
