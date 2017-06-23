package springer.paint.app.config

import springer.paint.canvas.{Canvas, CanvasDslInterpreter, CharCanvas}
import springer.paint.painter.Painter
import springer.paint.state.{PaintState, Uninitialised}
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
    lazy val painter = Painter[Char, String](plugins)
}

object Wiring extends Wiring