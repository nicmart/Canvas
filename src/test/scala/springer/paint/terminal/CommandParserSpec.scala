package springer.paint.terminal

import springer.paint.spec.CommonSpec

class CommandParserSpec extends CommonParserSpec {
    import CommandParser._
    "A CommandParser" should {
        "be able to combine with another one with an OR" in {
            (successful("a") or successful("b")).parse(Nil) shouldBe Success("a", Nil)
            (successful("a") or failing("e")).parse(Nil) shouldBe Success("a", Nil)
            (failing("e") or successful("a")).parse(Nil) shouldBe Success("a", Nil)
            (failing("a") or failing("b")).parse(Nil) shouldBe Failure("b")
        }
        "be able to map successful parsed values with a function" in {
            successful("a").map(_ * 2).parse(Nil) shouldBe Success("aa", Nil)
            failing("e").map(_ => "b").parse(Nil) shouldBe Failure("e")
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

    "A sequence parser" should {
        "parse a sequence of elements and return them in a list" in {
            val tokens = tokenize("10 20 30 40")
            val expected = Success(List(10, 20, 30), List("40"))
            sequenceOf(int, 3).parse(tokens) shouldBe expected
        }

        "fail when an element int the sequence is unparsable" in {
            val tokens = tokenize("10 20 aaa 40")
            inside(sequenceOf(int, 3).parse(tokens)) {
                case Failure(_) =>
            }
        }
    }
}