package springer.paint.terminal

import scala.util.Try
import scala.util.{Success => TrySuccess, Failure => TryFailure}

/**
  * The possible result of a parsing
  */
sealed trait ParserResult[+T] { self =>
    /**
      * Combine this result with another one, taking the first that succeeds, or tha last
      * failure
      */
    def or[S >: T](parserResult: => ParserResult[S]): ParserResult[S] =
        this match {
            case Success(_, _) => this
            case _ => parserResult
        }

    /**
      * Map a successful parser result into another result
      * Wrap any exception into a failure
      */
    def mapSuccess[S](f: Success[T] => ParserResult[S]): ParserResult[S] =
        this match {
            case success@Success(_, _) =>
                Try(f(success)) match {
                    case TrySuccess(s) => s
                    case TryFailure(e) => Failure(e.getMessage)
                }
            case failure@Failure(_) => failure
        }

    /**
      * Map the parsed value in case of success.
      */
    def map[S](f: T => S): ParserResult[S] = mapSuccess { success =>
        Success(f(success.value), success.tail)
    }
}

final case class Success[+T](value: T, tail: List[String] = Nil) extends ParserResult[T]
final case class Failure(error: String) extends ParserResult[Nothing]
