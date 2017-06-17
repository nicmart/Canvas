package springer.paint.dsl

/**
  * Created by Nicolò Martini on 29/04/2017.
  */
sealed trait TerminalDsl[T]

final case class GetCommand(prompt: String, invalid: String) extends TerminalDsl[String]
case object PrintCanvas extends TerminalDsl[Unit]
