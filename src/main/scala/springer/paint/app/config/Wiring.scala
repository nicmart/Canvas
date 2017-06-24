package springer.paint.app.config

import springer.paint.canvas.Canvas
import springer.paint.painter.Painter
import springer.paint.state.{PaintState, Uninitialised}
import springer.paint.plugin._

trait Wiring {
    type PainterOutput = Canvas[Char, String]
    type DefaultPaintState = PaintState[Char, String]
    lazy val initialState = Uninitialised[Char, String]()
    lazy val painter = Painter[Char, String](Map.empty)
        .addPlugin("C", NewCanvasPlugin, "Create New Canvas")
        .addPlugin("L", HorizontalLinePlugin, "Horizontal Line Plugin")
        .addPlugin("L", VerticalLinePlugin, "Vertical Line Plugin")
        .addPlugin("R", RectanglePlugin, "Rectangle Plugin")
        .addPlugin("B", FillPlugin, "Fill Plugin")
        .addPlugin("Q", QuitPlugin(), "Quit Plugin")
}

object Wiring extends Wiring