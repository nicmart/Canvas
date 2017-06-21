package springer.paint.dsl

import springer.paint.canvas.{Canvas, CanvasDslInterpreter, CharCanvas}
import springer.paint.state.{Initialised, PaintState}

/**
  * A PaintDsl interpreter
  *
  * @param canvasDslInterpreter The underying CanvasDSL Interpreter
  * @param empty An empty canvas factory
  * @tparam In The type of canvas inputs
  * @tparam Out The type of canvas outputs
  */
class PaintDslInterpreter[In, Out](
    canvasDslInterpreter: CanvasDslInterpreter[In, Out],
    empty: (Int, Int) => Canvas[In, Out]
) {
    def run(state: PaintState[In, Out], action: PaintDsl[In]): PaintState[In, Out] = {
        action match {
            case NewCanvas(width, height) =>
                Initialised(empty(width, height))
            case Draw(canvasAction) =>
                state.mapCanvas(canvasDslInterpreter.run(_, canvasAction))
        }
    }
}
