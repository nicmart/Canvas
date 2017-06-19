package springer.paint.terminal

import springer.paint.dsl.NewCanvas

/**
  * Created by Nicolò Martini <nicolo@martini.io>.
  */
class NewCanvasParserSpec extends BaseParserSpec {
    "A New Canvas Parser Spec" should {

        "parse a valid new canvas command" in {
            val tokens = tokenize("20 30")
            NewCanvasParser.parse(tokens) shouldBe Success(NewCanvas(20, 30), Nil)
        }

        "refuse non positive sizes" in {
            inside(NewCanvasParser.parse(tokenize("-20 30"))) {
                case Failure(_) =>
            }
            inside(NewCanvasParser.parse(tokenize("10 0"))) {
                case Failure(_) =>
            }
        }

        "refuse non valid integers" in {
            inside(NewCanvasParser.parse(tokenize("aaa 30"))) {
                case Failure(_) =>
            }
            inside(NewCanvasParser.parse(tokenize("10 bbb"))) {
                case Failure(_) =>
            }
        }
    }
}
