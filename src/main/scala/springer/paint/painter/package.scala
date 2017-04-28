package springer.paint

import springer.paint.dsl.PaintDsl
import springer.paint.state.PaintState

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
package object painter {
    type Painter[In, Out] = (PaintState[In, Out], PaintDsl) => PaintState[In, Out]
}
