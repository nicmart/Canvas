package springer.paint.terminal
import CommandParser._

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
    sealed trait CommandParserResult[+T] {
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
}
