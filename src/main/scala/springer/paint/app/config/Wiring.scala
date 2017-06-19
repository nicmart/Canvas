package springer.paint.app.config

import springer.paint.canvas.Canvas
import springer.paint.painter.{CanvasPainter, Painter}
import springer.paint.state.{PaintState, Uninitialised}
import springer.paint.terminal._
import Parser.{single, _}

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
trait Wiring {
    type PainterOutput = Canvas[Char, String]
    type DefaultPaintState = PaintState[Char, String]
    type DefaultPainter = Painter[Char, String]
    lazy val initialState = Uninitialised[Char, String]()
    lazy val painter = new CanvasPainter
    lazy val parser = {
        single("L", HorizontalLineParser or VerticalLineParser) or
        single("C", NewCanvasParser) or
        single("R", RectangleParser) or
        single("F", FillParser)
    }.flatten(Some(Failure("Command not recognized")))
}

object Wiring extends Wiring