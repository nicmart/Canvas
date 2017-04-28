package springer.paint.app.config

import springer.paint.canvas.Canvas
import springer.paint.canvas.Canvas
import springer.paint.painter.Painter
import springer.paint.state.{PaintState, Uninitialised}

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
trait Wiring {
    type PainterOutput = Canvas[Char, String]
    type DefaultPaintState = PaintState[Char, String]
    type DefaultPainter = Painter[Char, String]
    val initialState = Uninitialised[Char, String]()
}

object Wiring extends Wiring