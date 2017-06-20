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
      * Always parse the first token and return it
      */
    val first: Parser[String] =
        (tokens: List[String]) => tokens match {
            case head :: tail => Success(head, tail)
            case _ => Failure("No tokens found")
        }

    /**
      * A parser that parses a single token
      */
    def single[T](token: String, parsed: T): Parser[T] =
        first.filter(_ == token).map(_ => parsed)

    /**
      * Parse the first token, expecting it to be a single char
      */
    var char: Parser[Char] =
        first
            .filter(_.length == 1)
            .map(_.head)

    /**
      * Parse an integer
      */
    val int: Parser[Int] = first.map(_.toInt)

    /**
      * Parse a non-negative integer
      */
    val nonNegativeInt: Parser[Int] =
        int.filter(_ >= 0)

    /**
      * Parse a positive integer
      */
    val positiveInt: Parser[Int] =
        int.filter(_ > 0)

    /**
      * Parse an integer in a range
      */
    def rangeInt(from: Int, to: Int): Parser[Int] =
        int.filter(n => n >= from && n <= to)
}
