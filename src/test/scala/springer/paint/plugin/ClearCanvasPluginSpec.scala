package springer.paint.plugin

import springer.paint.canvas.Canvas
import springer.paint.parser.{Failure, Success}

class ClearCanvasPluginSpec extends BasePluginSpec {
    val plugin = ClearCanvasPlugin(' ')

    "The parser of the clear canvas plugin" should {
        val parser = plugin.commandParser
        "parse valid commands" in {
            val tokens = Nil
            val expectedCommand = plugin.ClearCanvasCommand
            parser.parse(tokens) shouldBe Success(expectedCommand, Nil)
        }
    }

    "The interpreter of the clear canvas pugin" should {
        val canvas = Canvas.filled(30, 10, 'x')
        "clear a canvas if we have one" in {
            val command = plugin.ClearCanvasCommand
            val expectedCanvas = Canvas.filled(30, 10, ' ')
            plugin.transformCanvas(command, canvas) shouldBe expectedCanvas
        }
        "add an error if we don't have a canvas yet" in {

        }
    }
}
