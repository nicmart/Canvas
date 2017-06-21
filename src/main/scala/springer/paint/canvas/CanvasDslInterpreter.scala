package springer.paint.canvas
import springer.paint.state.PaintState

/**
  * Interpret a CanvasDsl expression
  */
class CanvasDslInterpreter[In, Out] {
    /**
      * Draw a single point
      */
    def drawPoint(canvas: Canvas[In, Out], action: DrawPoint[In]): Canvas[In, Out] = {
        canvas.drawPoint(action.point, action.value)
    }

    /**
      * Draw any CanvasDsl action
      */
    def run(state: Canvas[In, Out], action: CanvasDsl[In]): Canvas[In, Out] =
        action match {
            case pointAction @ DrawPoint(_, _) => drawPoint(state, pointAction)
            case DrawSequence(actions) => actions.foldLeft(state)(run)
        }
}
