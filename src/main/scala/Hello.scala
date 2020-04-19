package demo

import cats.implicits._

object Hello extends App {

  val toLong: Int => Long       = _.toLong
  val toDouble: Float => Double = _.toDouble

  val f0: ((Int, Float)) => (Long, Double) = toLong *** toDouble

  val res0 = f0((3, 4.0f))
  println(res0)

  val addEmpty: Int => Int         = _ + 0
  val multiplyEmpty: Int => Double = _ * 1d

  val f1: Int => (Int, Double) = addEmpty &&& multiplyEmpty

  val res1 = f1(1)
  println(res1)

}
