package demo

import cats.implicits._

final case class Validator[T](din: T)

object BiApp extends App {
  val data: (Validator[Int], Validator[Int]) = (Validator[Int](1), Validator[Int](2))

  def plusOne(data: Validator[Int])  = data.copy(data.din + 1)
  def minusOne(data: Validator[Int]) = data.copy(data.din - 1)

  val out = data.bimap(plusOne, minusOne)

  println(data)
  println(out)
}
