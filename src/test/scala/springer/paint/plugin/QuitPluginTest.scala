package springer.paint.plugin

import springer.paint.canvas.CharCanvas
import springer.paint.state.{Final, Initialised, Uninitialised}
import springer.paint.terminal.{Failure, ParserSpec, Success}

class QuitPluginTest extends ParserSpec {
    val plugin = QuitPlugin[Char, String]()
    "The parser of the quit plugin" should {
        val parser = plugin.commandParser
        "parse valid commands" in {
            val tokens = tokenize("Q")
            val expectedCommand = plugin.Quit
            parser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }

        "refuse other malformed commands" in {
            val tokens = tokenize("10")
            inside(parser.parse(tokens)) {
                case Failure(_) =>
            }
        }
    }

    "The interpreter of a Quit Plugin" should {
        val interpreter = plugin.interpret _
        val command = plugin.Quit
        "finalise an uninitialised state" in {
            val start: NewCanvasPlugin.State = Uninitialised()
            val expected = Final()
            interpreter(command, start) shouldBe expected
        }
        "finalise an initialised state" in {
            val start: NewCanvasPlugin.State = Initialised(CharCanvas.empty(10, 10))
            val expected = Final()
            interpreter(command, start) shouldBe expected
        }
    }
}