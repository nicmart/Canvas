package springer.paint.terminal
import scala.util.control.Exception._
import CommandParser._

import scala.util.Try

/**
  * Parse a list of tokens into something else
  * This will at the end be used to expose a CommandParser[PaintDsl], i.e.
  * a parser whose return value is a Paint Command.
  */
trait CommandParser[+T] { self =>
    /**
      * Transform a list of token into a command parser result
      */
    def parse(tokens: List[String]): CommandParserResult[T]

    /**
      * Return a parser that in case of failure try to parse with fallbackParser
      */
    def or[S >: T](fallbackParser: CommandParser[S]): CommandParser[S] =
        (tokens: List[String]) => self.parse(tokens) or fallbackParser.parse(tokens)

    /**
      * Return a new parser that will return parsed values mapped with
      * the provided function
      */
    def map[S](f: T => S): CommandParser[S] =
        (tokens: List[String]) => self.parse(tokens).map(f)

    def flatMap[S](f: T => CommandParser[S]) =
        (tokens: List[String]) => self.parse(tokens).flatMap(f(_).parse(tokens))

    /**
      * Return a new parser whose successful result will be mapped through f
      */
    def mapSuccess[S](f: Success[T] => CommandParserResult[S]): CommandParser[S] =
        (tokens: List[String]) => self.parse(tokens).mapSuccess(f)

    def filter(predicate: T => Boolean, message: String): CommandParser[T] =
        mapSuccess { success => if (predicate(success.value)) success else Failure(message) }

    /**
      * Flatten a parser of parser of S into a parser of S
      */
    def flatten[S](failure: Option[Failure] = None)
        (implicit ev: T <:< CommandParser[S]): CommandParser[S] =
        (tokens: List[String]) => self.parse(tokens) match {
            case Success(parser, tail) => parser.parse(tail)
            case innerFailure@Failure(_) => failure.getOrElse(innerFailure)
        }
}

object CommandParser {
    /**
      * The possible result of a parsing
      */
    sealed trait CommandParserResult[+T] { self =>
        /**
          * Combine this result with another one, taking the first that succeeds, or tha last
          * failure
          */
        def or[S >: T](
            parserResult: => CommandParserResult[S]
        ): CommandParserResult[S] = this match {
            case Success(_, _) => this
            case _ => parserResult
        }

        /**
          * Map the parsed value in case of success
          */
        def map[S](f: T => S): CommandParserResult[S] = mapSuccess { success =>
            Success(f(success.value), success.tail)
        }

        def flatMap[S](f: T => CommandParserResult[S]): CommandParserResult[S] =
            mapSuccess { success => f(success.value) }

        /**
          * Map a successful parser result into another result
          */
        def mapSuccess[S](f: Success[T] => CommandParserResult[S]): CommandParserResult[S] = this match {
            case success@Success(_, _) => f(success)
            case failure@Failure(_) => failure
        }

    }
    final case class Success[+T](value: T, tail: List[String]) extends CommandParserResult[T]
    final case class Failure(error: String) extends CommandParserResult[Nothing]

    /**
      * An always successful parser that does not consume any token
      */
    def successful[T](value: T): CommandParser[T] =
        (tokens: List[String]) => Success(value, tokens)

    /**
      * An always-failing parser
      */
    def failing(error: String): CommandParser[Nothing] =
        _ => Failure(error)

    /**
      * A parser that parses a single token
      */
    def single[T](token: String, parsed: T): CommandParser[T] =
        (tokens: List[String]) => tokens match {
            case head :: tail if head == token =>
                Success(parsed, tail)
            case _ => Failure(s"Token $token not found")
        }

    val int: CommandParser[Int] =
        (tokens: List[String]) => tokens match {
            case head :: tail =>
                Try(head.toInt)
                    .map(Success(_, tail))
                    .getOrElse(Failure("Not a valid integer"))
            case _ =>
                Failure("No tokens found")
        }

    val nonNegativeInt: CommandParser[Int] =
        int.filter(_ >= 0, "Integer must be non-negative")

    val positiveInt: CommandParser[Int] =
        int.filter(_ > 0, "Integer must be positive")

    def rangeInt(from: Int, to: Int): CommandParser[Int] =
        int.filter(n => n >= from && n <= to, s"Integer must be between $from and $to")

    /**
      * Parse a sequence of tokens with the same parser, and collect the result
      * in a list. In case of failure, return the first failure.
      */
    def times[T](parser: CommandParser[T], n: Int): CommandParser[List[T]] =
        sequence(List.fill(n)(parser): _*)

    /**
      * Apply one parser after the other, and collect the results in a list
      */
    def sequence[T](parsers: CommandParser[T]*): CommandParser[List[T]] = {
        parsers.toList match {
            case parser :: tail =>
                combine(parser, sequence(tail: _*))(_ :: _)
            case Nil => successful(Nil)
        }
    }

    def combine[T, S, U](
        parser1: CommandParser[T],
        parser2: CommandParser[S])(
        f: (T, S) => U
    ): CommandParser[U] = {
        parser1.mapSuccess { success =>
            parser2.parse(success.tail).map(f(success.value, _))
        }
    }
}
