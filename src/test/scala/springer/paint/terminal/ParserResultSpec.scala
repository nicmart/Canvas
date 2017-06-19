package springer.paint.terminal

/**
  * Created by Nicolò Martini <nicolo@martini.io>.
  */
class ParserResultSpec extends CommonParserSpec{
    "A Successful parser result" should {
        "return itself when or-ed with any another result" in {
            val successful1 = Success(123)
            val successful2 = Success(111)
            val failure = Failure("e")
            successful1 or successful2 shouldBe successful1
            successful1 or failure shouldBe successful1
        }

        "apply the function passed to mapSuccess to itself" in {
            val successful = Success(123)
            val mapped = successful.mapSuccess(s => Failure(s.value.toString))
            mapped shouldBe Failure("123")
        }

        "apply the function passed to map to its value" in {
            val successful = Success(123)
            val mapped = successful.map(_ + 1)
            mapped shouldBe Success(124)
        }
    }

    "A Failure parser result" should {
        "return the other result when or-ed" in {
            val successful = Success(123)
            val failure = Failure("e")
            val failure2 = Failure("ee")
            failure or successful shouldBe successful
            failure or failure2 shouldBe failure2
        }

        "remain unchanged when mapSuccess is called" in {
            val failure = Failure("e")
            val mapped = failure.mapSuccess(s => Success(123))
            mapped shouldBe failure
        }

        "remain unchanged when map is called" in {
            val failure: ParserResult[Int] = Failure("e")
            val mapped = failure.map(_ + 1)
            mapped shouldBe failure
        }
    }
}
