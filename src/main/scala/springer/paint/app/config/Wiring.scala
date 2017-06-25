package springer.paint.app.config

import springer.paint.canvas.Canvas
import springer.paint.painter.Painter
import springer.paint.state.{PaintState, Uninitialised}
import springer.paint.plugin.{FillPlugin, HorizontalLinePlugin, RectanglePlugin, VerticalLinePlugin, _}
import springer.paint.terminal.CommonParsers._

trait Wiring {
    type DefaultPaintState = PaintState[Char]
    lazy val initialState = Uninitialised[Char]()
    lazy val lineSymbol = 'x'

    // Plugins
    lazy val newCanvasPlugin = NewCanvasPlugin
    lazy val horizontalLinePlugin = HorizontalLinePlugin(lineSymbol)
    lazy val verticalLinePlugin = VerticalLinePlugin(lineSymbol)
    lazy val rectanglePlugin = RectanglePlugin(horizontalLinePlugin, verticalLinePlugin)
    lazy val fillPlugin = FillPlugin(char)
    lazy val quitPlugin = QuitPlugin[Char]()

    /**
      * Build a painter object with the desired plugins
      */
    lazy val painter = Painter[Char](Map.empty)
        .addPlugin("C", newCanvasPlugin)
        .addPlugin("L", horizontalLinePlugin)
        .addPlugin("L", verticalLinePlugin)
        .addPlugin("R", rectanglePlugin)
        .addPlugin("B", fillPlugin)
        .addPlugin("Q", quitPlugin)
}

object Wiring extends Wiring