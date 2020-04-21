package demo
import java.util.UUID

import cats.implicits._

object implicits {
  final case class UserClass(id: UUID, list: List[Int])

  implicit class tup2Ops[A, B](tup: Tuple2[A, B]) {
    def lmap[C](f: A => C): Tuple2[C, B]                     = tup.leftMap(f)
    def rmap[C](f: B => C): Tuple2[A, C]                     = tup.swap.leftMap(f).swap
    def bmap[A1, B1](f: A => A1, g: B => B1): Tuple2[A1, B1] = tup.bimap(f, g)

    // def rightMap[C](tup: Tuple2[A, B])(f: B => C) = tup.bifoldMap(identity[A], f)

  }

  implicit class userOps(user: UserClass) {
    private val tup = (user.id, user.list)

    def lmap(f: UUID => Int)                            = tup.leftMap(f)
    def rmap(f: List[Int] => List[Int])                 = tup.swap.leftMap(f).swap
    def bmap(f: UUID => Int, g: List[Int] => List[Int]) = tup.bimap(f, g)

  }
}
