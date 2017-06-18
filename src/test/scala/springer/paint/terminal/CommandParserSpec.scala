package springer.paint.terminal

import springer.paint.spec.CommonSpec

class CommandParserSpec extends CommonSpec {
    import CommandParser._
    "A CommandParser" should {
        "be able to combine with another one with an OR" in {
            (successful("a") or successful("b")).parse(Nil) shouldBe Success("a", Nil)
            (successful("a") or failing("e")).parse(Nil) shouldBe Success("a", Nil)
            (failing("e") or successful("a")).parse(Nil) shouldBe Success("a", Nil)
            (failing("a") or failing("b")).parse(Nil) shouldBe Failure("b")
        }
    }

    "A CommandParser of a CommandParser" should {
        "concatenate the parsers when flattened" in {
            val parser: CommandParser[CommandParser[String]] =
                (tokens: List[String]) => Success(successful(tokens.headOption.getOrElse("")), tokens)

            parser.flatten().parse(List("a", "b")) shouldBe Success("a", List("a", "b"))
        }
    }

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
