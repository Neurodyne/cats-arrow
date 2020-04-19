package console

import scala.Function.tupled

import cats.arrow.Arrow
import cats.implicits._

sealed trait ConsoleArr[X, Y]

object ConsoleArr {
  case class Lift[A, B](f: A => B)                                                extends ConsoleArr[A, B]
  case class First[A, B, C](fa: ConsoleArr[A, B])                                 extends ConsoleArr[(A, C), (B, C)]
  case class AndThen[A, B, C](start: ConsoleArr[A, B], next: ConsoleArr[B, C])    extends ConsoleArr[A, C]
  case class Split[A, B, C, D](first: ConsoleArr[A, B], second: ConsoleArr[C, D]) extends ConsoleArr[(A, C), (B, D)]
  case object GetLine                                                             extends ConsoleArr[Unit, String]
  case object PutLine                                                             extends ConsoleArr[String, Unit]

  implicit val arrow: Arrow[ConsoleArr] = new Arrow[ConsoleArr] {

    override def compose[A, B, C](f: ConsoleArr[B, C], g: ConsoleArr[A, B]): ConsoleArr[A, C] = AndThen(g, f)

    override def first[A, B, C](fa: ConsoleArr[A, B]): ConsoleArr[(A, C), (B, C)] = First(fa)
    override def lift[A, B](f: A => B): ConsoleArr[A, B]                          = Lift(f)

  }

  val getLine: ConsoleArr[Unit, String] = GetLine
  val putLine: ConsoleArr[String, Unit] = PutLine

  val concat: ConsoleArr[(String, String), String] = Lift(tupled(_ + _))
  val show: ConsoleArr[Int, String]                = Lift(_.toString)
  val plus: ConsoleArr[(Int, Int), Int]            = Lift(tupled(_ + _))
  val divMod: ConsoleArr[(Int, Int), (Int, Int)]   = Lift { case (x, y) => (x / y, x % y) }

  val echo2: ConsoleArr[Unit, Unit] =
    (getLine &&& getLine) >>> concat >>> putLine

  def countGets[X, Y](carr: ConsoleArr[X, Y]): Int =
    carr match {
      case Lift(_) => 0
      case AndThen(start, next) =>
        countGets(start) + countGets(next)
      case First(_) => 0
      case Split(first, second) =>
        countGets(first) + countGets(second)
      case GetLine => 1
      case PutLine => 0
    }

}

object App0 extends App {
  val res0 = ConsoleArr.echo2
}
