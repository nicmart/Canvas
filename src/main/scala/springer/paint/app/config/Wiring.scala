package springer.paint.app.config

import springer.paint.app.PaintApp
import springer.paint.canvas.{BorderCanvasRenderer, Canvas}
import springer.paint.painter.Painter
import springer.paint.state.{PaintState, Uninitialised}
import springer.paint.plugin.{FillPlugin, HorizontalLinePlugin, RectanglePlugin, VerticalLinePlugin, _}
import springer.paint.parser.CommonParsers._

trait Wiring {
    type DefaultPaintState = PaintState[Char]
    lazy val welcome = "Welcome to painter!"
    lazy val initialState = Uninitialised.addOutput(welcome)

    // Default symbols for lines and white pixels
    lazy val lineSymbol = 'x'
    lazy val emptySymbol = ' '

    // Plugins
    lazy val newCanvasPlugin = NewCanvasPlugin(emptySymbol)
    lazy val horizontalLinePlugin = HorizontalLinePlugin(lineSymbol)
    lazy val verticalLinePlugin = VerticalLinePlugin(lineSymbol)
    lazy val rectanglePlugin = RectanglePlugin(horizontalLinePlugin, verticalLinePlugin)
    lazy val fillPlugin = FillPlugin(char)
    lazy val quitPlugin = QuitPlugin
    lazy val clearCanvasPlugin = ClearCanvasPlugin(emptySymbol)

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
        .addPlugin("C", clearCanvasPlugin)
        .addPlugin("U", UndoPlugin[Char]())

    lazy val renderer = BorderCanvasRenderer('-', '|', '-', '-', '-', '-')
    lazy val app = PaintApp(initialState, painter, renderer)
}

object Wiring extends Wiring