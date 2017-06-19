package springer.paint.terminal

import scala.util.Try

/**
  * Created by Nicolò Martini <nicolo@martini.io>.
  *
  * Simple parsers definitions
  */
object CommonParsers {
    /**
      * An always successful parser that does not consume any token
      */
    def successful[T](value: T): Parser[T] =
        (tokens: List[String]) => Success(value, tokens)

    /**
      * An always-failing parser
      */
    def failing(error: String): Parser[Nothing] =
        _ => Failure(error)

    /**
      * A parser that parses a single token
      */
    def single[T](token: String, parsed: T): Parser[T] =
        (tokens: List[String]) => tokens match {
            case head :: tail if head == token =>
                Success(parsed, tail)
            case _ => Failure(s"Token $token not found")
        }

    /**
      * Always parse the first token and return it
      */
    val first: Parser[String] =
        (tokens: List[String]) => tokens match {
            case head :: tail => Success(head, tail)
            case _ => Failure("No tokens found")
        }

    /**
      * Parse the first token, expecting it to be a single char
      */
    var char: Parser[Char] =
        first
            .filter(_.length == 1, "Expected a string of length 1")
            .map(_.headOption.getOrElse(' '))

    /**
      * Parse an integer
      */
    val int: Parser[Int] =
        (tokens: List[String]) => tokens match {
            case head :: tail =>
                Try(head.toInt)
                    .map(Success(_, tail))
                    .getOrElse(Failure("Not a valid integer"))
            case _ =>
                Failure("No tokens found")
        }

    /**
      * Parse a non-negative integer
      */
    val nonNegativeInt: Parser[Int] =
        int.filter(_ >= 0, "Integer must be non-negative")

    /**
      * Parse a positive integer
      */
    val positiveInt: Parser[Int] =
        int.filter(_ > 0, "Integer must be positive")

    /**
      * Parse an integer in a range
      */
    def rangeInt(from: Int, to: Int): Parser[Int] =
        int.filter(n => n >= from && n <= to, s"Integer must be between $from and $to")
}
