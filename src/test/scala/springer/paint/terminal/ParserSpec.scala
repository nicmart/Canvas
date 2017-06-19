package springer.paint.terminal
import Parser._
import CommonParsers._

class ParserSpec extends BaseParserSpec {

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

    "A nested parser" should {
        "concatenate the parsers when flattened" in {
            val parser: Parser[Parser[String]] =
                (tokens: List[String]) => Success(successful(tokens.headOption.getOrElse("")), tokens)

            parser.flatten().parse(List("a", "b")) shouldBe Success("a", List("a", "b"))
        }
    }

    "A sequence parser" should {
        "parse a sequence of elements and return them in a list" in {
            val tokens = tokenize("10 a 40")
            val expected = Success(List(10, 11), List("40"))
            sequence[Int](int, single("a", 11)).parse(tokens) shouldBe expected
        }

        "fail when an element int the sequence is unparsable" in {
            val tokens = tokenize("10 b aaa 40")
            inside(sequence(int, single("a", 10)).parse(tokens)) {
                case Failure(_) =>
            }
        }
    }

    "A times parser" should {
        "parse a sequence of elements and return them in a list" in {
            val tokens = tokenize("10 20 30 40")
            val expected = Success(List(10, 20, 30), List("40"))
            times(int, 3).parse(tokens) shouldBe expected
        }

        "fail when an element int the sequence is unparsable" in {
            val tokens = tokenize("10 20 aaa 40")
            inside(times(int, 3).parse(tokens)) {
                case Failure(_) =>
            }
        }
    }

    "A combined parser" should {
        "apply the two parsers in sequence, and the apply the combining function" in {
            val parser = combine(int, int)(_ + _)
            parser.parse(tokenize("10 20 30")) shouldBe Success(30, List("30"))
        }

        "fail if the first parser fails" in {
            val parser = combine(failing("e"): Parser[Int], int)(_ + _)
            parser.parse(tokenize("10 20 30")) shouldBe Failure("e")
        }

        "fail if the second parser fails" in {
            val parser = combine(int, failing("e"): Parser[Int])(_ + _)
            parser.parse(tokenize("10 20 30")) shouldBe Failure("e")
        }

    }
}
