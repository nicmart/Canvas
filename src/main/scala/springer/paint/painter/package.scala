package springer.paint

import springer.paint.dsl.PaintDslLegacy
import springer.paint.state.PaintState

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
package object painter {
    type Painter[In, Out] = (PaintState[In, Out], PaintDslLegacy) => PaintState[In, Out]
}
