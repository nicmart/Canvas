package springer.paint.app.config

import springer.paint.canvas.{Canvas, CanvasDslInterpreter, CharCanvas}
import springer.paint.painter.{CanvasPainter, NewPainter, Painter}
import springer.paint.state.{PaintState, Uninitialised}
import springer.paint.terminal._
import CommonParsers._
import springer.paint.dsl.PaintDslInterpreter
import springer.paint.painter
import springer.paint.plugin.{HorizontalLinePlugin, VerticalLinePlugin}

trait Wiring {
    type PainterOutput = Canvas[Char, String]
    type DefaultPaintState = PaintState[Char, String]
    type DefaultPainter = Painter[Char, String]
    lazy val initialState = Uninitialised[Char, String]()
    lazy val painterOld = new CanvasPainter
    lazy val plugins = List(
        HorizontalLinePlugin,
        VerticalLinePlugin
    )
    lazy val canvasDslInterpreter = new CanvasDslInterpreter[Char, String]
    lazy val paintDslInterpreter = new PaintDslInterpreter[Char, String](canvasDslInterpreter, CharCanvas.empty)
    lazy val painter = NewPainter[Char, String](paintDslInterpreter, plugins)
    lazy val parser = {
        single("L", HorizontalLineParser or VerticalLineParser) or
        single("C", NewCanvasParser) or
        single("R", RectangleParser) or
        single("F", FillParser)
    }.flatten(Some(Failure("Command not recognized")))
}

object Wiring extends Wiring