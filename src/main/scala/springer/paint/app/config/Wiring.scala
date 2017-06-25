package springer.paint.app.config

import springer.paint.canvas.Canvas
import springer.paint.painter.Painter
import springer.paint.state.{PaintState, Uninitialised}
import springer.paint.plugin._

trait Wiring {
    type PainterOutput = Canvas[Char]
    type DefaultPaintState = PaintState[Char]
    lazy val initialState = Uninitialised[Char]()

    /**
      * Build a painter object with the desired plugins
      */
    lazy val painter = Painter[Char](Map.empty)
        .addPlugin("C", NewCanvasPlugin)
        .addPlugin("L", HorizontalLinePlugin)
        .addPlugin("L", VerticalLinePlugin)
        .addPlugin("R", RectanglePlugin)
        .addPlugin("B", FillPlugin)
        .addPlugin("Q", QuitPlugin())
}

object Wiring extends Wiring