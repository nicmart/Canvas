package springer.paint.terminal

import springer.paint.terminal.CommonParsers.{int, single}

/**
  * Created by Nicolò Martini <nicolo@martini.io>.
  */
class CommonParsersSpec extends BaseParserSpec{
    "A Single token parser" should {
        "parse the first token by exact match" in {
            val parser = single("a", 123)
            parser.parse(List("a", "b", "c")) shouldBe Success(123, List("b", "c"))
            inside(parser.parse(List("x", "b", "c"))) {
                case Failure(_) =>
            }
        }
    }

    "An int parser" should {
        "parse a valid int" in {
            int.parse(List("123")) shouldBe Success(123, Nil)
        }
        "refuse an invalid int" in {
            inside(int.parse(List("aaa"))) { case Failure(_) => }
        }
    }
}
