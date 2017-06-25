package springer.paint.parser

import springer.paint.point.Point
import springer.paint.parser.Parser.combine

import scala.util.Try

/**
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
    def failing(error: String = ""): Parser[Nothing] =
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
      * Match the first token
      */
    def matchFirst(token: String): Parser[String] =
        first.filter(_ == token)

    /**
      * Parse the first token, expecting it to be a single char
      */
    val char: Parser[Char] =
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

    def point: Parser[Point] =
        combine(int, int)(Point)
}
