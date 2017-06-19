package springer.paint.terminal

import springer.paint.terminal.CommonParsers._

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

    "`first` parser" should {
        "return the first token" in {
            first.parse(tokenize("hello world")) shouldBe Success("hello", List("world"))
        }
        "fail if no tokens available" in {
            inside(first.parse(Nil)) { case Failure(_) => }
        }
    }

    "`char` parser" should {
        "match the first token if it is a single character" in {
            char.parse(List("x")) shouldBe Success('x')
        }
        "fail if the first token has more than one char" in {
            inside(char.parse(List("xx"))) { case Failure(_) => }
        }
        "fail if there are no tokens available" in {
            inside(char.parse(Nil)) { case Failure(_) => }
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

    "A non-negative int parser" should {
        "parse a valid non-negative int" in {
            nonNegativeInt.parse(List("0")) shouldBe Success(0, Nil)
        }
        "refuse a negative int" in {
            inside(nonNegativeInt.parse(List("-1"))) { case Failure(_) => }
        }
        "refuse an invalid int" in {
            inside(nonNegativeInt.parse(List("aaa"))) { case Failure(_) => }
        }
    }

    "A positive int parser" should {
        "parse a valid positive int" in {
            positiveInt.parse(List("12")) shouldBe Success(12, Nil)
        }
        "refuse a non positive int" in {
            inside(positiveInt.parse(List("0"))) { case Failure(_) => }
        }
        "refuse an invalid int" in {
            inside(positiveInt.parse(List("aaa"))) { case Failure(_) => }
        }
    }
}
