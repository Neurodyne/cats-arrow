package bench

import java.util.UUID
import java.util.concurrent.TimeUnit

import Helper._
import cats.implicits._
import demo.implicits._
import org.openjdk.jmh.annotations._
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class ArrBenchmark {
  @Benchmark
  def lmap = tup.lmap(combined)

  @Benchmark
  def rmapComposed = tup.rmap(combined)

  @Benchmark
  def bmap = tup.bmap(identity[Int], combined)

  @Benchmark
  def rmapBM = tup.bifoldMap(identity[Int], combined)
}

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class GRBenchmark {
  @Benchmark
  def lmap = user.lmap(procUUID)

  @Benchmark
  def rmap = user.rmap(procList)

  @Benchmark
  def bmap = user.bmap(procUUID, procList)
}

object Helper {

  // Arrow Bench
  val addOne   = (_: Int) + 1
  val mulTwo   = (_: Int) * 2
  val combined = addOne >>> mulTwo

  val tup = (1, 1)

  // GR bench
  def procUUID(id: UUID): Int              = id.hashCode()
  def procList(list: List[Int]): List[Int] = list.map(_ * 3)

  val list = Range(1, 10000).toList
  val user = UserClass(UUID.randomUUID, list)

}

object App0 extends App {

  val res0 = tup.lmap(combined)
  val res1 = tup.rmap(combined)
  val res2 = tup.bimap(identity[Int], combined)
  val res3 = tup.bifoldMap(identity[Int], combined)

  println(res0)
  println(res1)
  println(res2)
  println(res3)
}
