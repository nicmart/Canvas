package springer.paint.parser

/**
  * Parse a list of tokens into something else
  * This will at the end be used to expose a
  * a parser whose return value is a Paint Command.
  */
trait Parser[+T] { self =>
    /**
      * Transform a list of token into a command parser result
      */
    def parse(tokens: List[String]): ParserResult[T]

    /**
      * Return a new parser whose successful result will be mapped through f
      */
    def mapSuccess[S](f: Success[T] => ParserResult[S]): Parser[S] =
        (tokens: List[String]) => self.parse(tokens).mapSuccess(f)

    /**
      * Return a new parses whose failures are mapped with f
      */
    def mapFailure[S >: T](f: Failure => ParserResult[S]): Parser[S] =
        (tokens: List[String]) => self.parse(tokens).mapFailure[S](f)

    /**
      * Return a new parser that will return parsed values mapped with
      * the provided function
      */
    def map[S](f: T => S): Parser[S] =
        mapSuccess(success => Success(f(success.value), success.tail))

    /**
      * Try this parser, and in case of failure try the other one
      */
    def or[S >: T](fallbackParser: Parser[S]): Parser[S] =
        (tokens: List[String]) => self.parse(tokens) or fallbackParser.parse(tokens)

    /**
      * Apply this parser, and then, depending on its result,
      * generate a second one that will parse the rest of the tokens
      */
    def andThen[S](f: T => Parser[S]): Parser[S] =
        mapSuccess { case Success(t, tail) => f(t).parse(tail) }

    /**
      * Change the error message of failures into {error}
      */
    def label(error: String): Parser[T] =
        mapFailure(_ => Failure(error))

    /**
      * Finalise this parser, making it fail if it does not consume
      * all the tokens
      */
    def finalise(error: String = ""): Parser[T] =
        mapSuccess {
            case success@Success(value, Nil) => success
            case _ => Failure(error)
        }

    /**
      * Make the parser fail if the parsed result does not match the predicate
      */
    def filter(predicate: T => Boolean, message: String = ""): Parser[T] =
        mapSuccess { success => if (predicate(success.value)) success else Failure(message) }
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

    /**
      * Combine the result of two successful parsers
      */
    def combine[T, S, U](parser1: Parser[T], parser2: Parser[S])
        (f: (T, S) => U): Parser[U] = {
        parser1.mapSuccess { success =>
            parser2.parse(success.tail).map(f(success.value, _))
        }
    }

    /**
      * Apply two parsers in sequence, and get the result as a pair
      */
    def pair[T, S](parser1: Parser[T], parser2: Parser[S]): Parser[(T, S)] =
        combine(parser1, parser2)((_, _))
}
