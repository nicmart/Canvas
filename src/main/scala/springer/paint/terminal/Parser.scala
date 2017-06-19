package springer.paint.terminal

/**
  * Parse a list of tokens into something else
  * This will at the end be used to expose a CommandParser[PaintDsl], i.e.
  * a parser whose return value is a Paint Command.
  */
trait Parser[+T] { self =>
    /**
      * Transform a list of token into a command parser result
      */
    def parse(tokens: List[String]): ParserResult[T]

    /**
      * Return a parser that in case of failure try to parse with fallbackParser
      */
    def or[S >: T](fallbackParser: Parser[S]): Parser[S] =
        (tokens: List[String]) => self.parse(tokens) or fallbackParser.parse(tokens)

    /**
      * Return a new parser that will return parsed values mapped with
      * the provided function
      */
    def map[S](f: T => S): Parser[S] =
        (tokens: List[String]) => self.parse(tokens).map(f)

    /**
      * Return a new parser whose successful result will be mapped through f
      */
    def mapSuccess[S](f: Success[T] => ParserResult[S]): Parser[S] =
        (tokens: List[String]) => self.parse(tokens).mapSuccess(f)

    def filter(predicate: T => Boolean, message: String): Parser[T] =
        mapSuccess { success => if (predicate(success.value)) success else Failure(message) }

    /**
      * Flatten a parser of parser of S into a parser of S
      */
    def flatten[S](failure: Option[Failure] = None)
        (implicit ev: T <:< Parser[S]): Parser[S] =
        (tokens: List[String]) => self.parse(tokens) match {
            case Success(parser, tail) => parser.parse(tail)
            case innerFailure@Failure(_) => failure.getOrElse(innerFailure)
        }
}

object Parser {
    import CommonParsers._
    /**
      * Parse a sequence of tokens with the same parser, and collect the result
      * in a list. In case of failure, return the first failure.
      */
    def times[T](parser: Parser[T], n: Int): Parser[List[T]] =
        sequence(List.fill(n)(parser): _*)

    /**
      * Apply one parser after the other, and collect the results in a list
      */
    def sequence[T](parsers: Parser[T]*): Parser[List[T]] = {
        parsers.toList match {
            case parser :: tail =>
                combine(parser, sequence(tail: _*))(_ :: _)
            case Nil => successful(Nil)
        }
    }

    def combine[T, S, U](
        parser1: Parser[T],
        parser2: Parser[S])(
        f: (T, S) => U
    ): Parser[U] = {
        parser1.mapSuccess { success =>
            parser2.parse(success.tail).map(f(success.value, _))
        }
    }
}
