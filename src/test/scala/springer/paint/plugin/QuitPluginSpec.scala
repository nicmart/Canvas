package springer.paint.plugin

import springer.paint.canvas.Canvas
import springer.paint.state.{Final, Initialised, Uninitialised}
import springer.paint.parser.{Failure, ParserSpec, Success}

class QuitPluginSpec extends BasePluginSpec {
    val plugin = QuitPlugin
    "The parser of the quit plugin" should {
        val parser = plugin.commandParser
        "parse valid commands" in {
            val tokens = Nil
            val expectedCommand = plugin.Quit
            parser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }

//        "refuse other malformed commands" in {
//            val tokens = tokenize("10")
//            inside(parser.parse(tokens)) {
//                case Failure(_) =>
//            }
//        }
    }

    "The interpreter of a Quit Plugin" should {
        val interpreter = plugin.interpret[Any] _
        val command = plugin.Quit
        "finalise an uninitialised state" in {
            val start = Uninitialised
            val expected = Final
            interpreter(command, start).consumeOutput._2 shouldBe expected
        }
        "finalise an initialised state" in {
            val start = Initialised(Canvas.filled(10, 10, ' '))
            val expected = Final
            interpreter(command, start).consumeOutput._2 shouldBe expected
        }
    }
}
