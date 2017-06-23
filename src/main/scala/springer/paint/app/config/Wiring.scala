package springer.paint.app.config

import springer.paint.canvas.{Canvas, CanvasDslInterpreter, CharCanvas}
import springer.paint.painter.NewPainter
import springer.paint.state.{PaintState, Uninitialised}
import springer.paint.dsl.PaintDslInterpreter
import springer.paint.plugin._

trait Wiring {
    type PainterOutput = Canvas[Char, String]
    type DefaultPaintState = PaintState[Char, String]
    lazy val initialState = Uninitialised[Char, String]()
    lazy val plugins = List(
        NewCanvasPlugin,
        HorizontalLinePlugin,
        VerticalLinePlugin,
        RectanglePlugin,
        FillPlugin
    )
    lazy val canvasDslInterpreter = new CanvasDslInterpreter[Char, String]
    lazy val paintDslInterpreter = new PaintDslInterpreter[Char, String](canvasDslInterpreter, CharCanvas.empty)
    lazy val painter = NewPainter[Char, String](plugins)
}

object Wiring extends Wiring