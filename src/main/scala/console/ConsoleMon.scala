package console

import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.{ Monad, StackSafeMonad }

sealed trait ConsoleM[X]

object ConsoleM {
  case class Pure[A](x: A)                                     extends ConsoleM[A]
  case class Bind[A, B](x: ConsoleM[A], fab: A => ConsoleM[B]) extends ConsoleM[B]
  case object GetLine                                          extends ConsoleM[String]
  case class PutLine(s: String)                                extends ConsoleM[Unit]

  implicit val monad: Monad[ConsoleM] =
    new StackSafeMonad[ConsoleM] {
      def pure[A](x: A): ConsoleM[A]                                       = Pure(x)
      def flatMap[A, B](fa: ConsoleM[A])(f: A => ConsoleM[B]): ConsoleM[B] = Bind(fa, f)
    }

  val getLine: ConsoleM[String]          = GetLine
  def putLine(s: String): ConsoleM[Unit] = PutLine(s)

  def echo2: ConsoleM[String] =
    for {
      x <- getLine
      y <- getLine
      _ <- putLine(x + y)
    } yield x + y

  def countGets[A](cm: ConsoleM[A]): Int = cm match {
    case Pure(_)    => 0
    case GetLine    => 1
    case PutLine(_) => 0
    case Bind(m, f) => countGets(m) + (??? : Int)
  }
}
