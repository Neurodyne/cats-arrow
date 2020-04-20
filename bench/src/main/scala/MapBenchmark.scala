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
  def lmap = tup.lmap(addOne >>> mulTwo)

  @Benchmark
  def rmap = tup.rmap(addOne >>> mulTwo)

  @Benchmark
  def bmap = tup.bmap(addOne >>> mulTwo, identity[Int] _ >>> mulTwo)
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
  val addOne = (_: Int) + 1
  val mulTwo = (_: Int) * 2

  val tup = (-1, 2)

  // GR bench
  def procUUID(id: UUID): Int              = id.hashCode()
  def procList(list: List[Int]): List[Int] = list.map(_ * 3)

  val list = Range(1, 10000).toList
  val user = UserClass(UUID.randomUUID, list)

}
