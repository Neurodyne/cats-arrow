package console

import cats.syntax.apply._
import cats.{ Applicative }

sealed trait ConsoleA[X]

object ConsoleA {
  case class Pure[A](x: A)                                 extends ConsoleA[A]
  case class Ap[A, B](f: ConsoleA[A => B], x: ConsoleA[A]) extends ConsoleA[B]
  case object GetLine                                      extends ConsoleA[String]
  case class PutLine(s: String)                            extends ConsoleA[Unit]

  implicit val applicative: Applicative[ConsoleA] =
    new Applicative[ConsoleA] {
      def pure[A](x: A): ConsoleA[A]                                   = Pure(x)
      def ap[A, B](ff: ConsoleA[A => B])(fa: ConsoleA[A]): ConsoleA[B] = Ap(ff, fa)
    }

  val getLine: ConsoleA[String]          = GetLine
  def putLine(s: String): ConsoleA[Unit] = PutLine(s)

  def countGets[X](ca: ConsoleA[X]): Int = ca match {
    case Pure(_)    => 0
    case GetLine    => 1
    case PutLine(_) => 0
    case Ap(f, x)   => countGets(f) + countGets(x)
  }

  def echo2: ConsoleA[Unit] = {
    (getLine, getLine).tupled
    putLine(???)
  }
}
