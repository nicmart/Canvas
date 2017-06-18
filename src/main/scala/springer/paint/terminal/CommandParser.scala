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

    /**
      * Return a new parser whose successful result will be mapped through f
      */
    def mapSuccess[S](f: Success[T] => CommandParserResult[S]): CommandParser[S] =
        (tokens: List[String]) => self.parse(tokens).mapSuccess(f)

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

    def sequenceOf[T](parser: CommandParser[T], n: Int): CommandParser[List[T]] = {
        n match {
            case _ if n <= 0 => failing("Impossible applying a parser 0 or less times")
            case _ if n == 1 => parser.map(List(_))
            case _ => (tokens: List[String]) =>
                parser.parse(tokens).mapSuccess { success =>
                    sequenceOf(parser, n - 1).parse(success.tail).map(success.value :: _)
                }
        }
    }
}
