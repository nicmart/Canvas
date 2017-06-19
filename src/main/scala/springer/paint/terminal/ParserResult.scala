package springer.paint.terminal

/**
  * The possible result of a parsing
  */
sealed trait ParserResult[+T] { self =>
    /**
      * Combine this result with another one, taking the first that succeeds, or tha last
      * failure
      */
    def or[S >: T](
        parserResult: => ParserResult[S]
    ): ParserResult[S] = this match {
        case Success(_, _) => this
        case _ => parserResult
    }

    /**
      * Map the parsed value in case of success
      */
    def map[S](f: T => S): ParserResult[S] = mapSuccess { success =>
        Success(f(success.value), success.tail)
    }

    def flatMap[S](f: T => ParserResult[S]): ParserResult[S] =
        mapSuccess { success => f(success.value) }

    /**
      * Map a successful parser result into another result
      */
    def mapSuccess[S](f: Success[T] => ParserResult[S]): ParserResult[S] = this match {
        case success@Success(_, _) => f(success)
        case failure@Failure(_) => failure
    }

}

final case class Success[+T](value: T, tail: List[String]) extends ParserResult[T]
final case class Failure(error: String) extends ParserResult[Nothing]
