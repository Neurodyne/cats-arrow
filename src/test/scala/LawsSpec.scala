package laws

/**
 * Laws are taken from "Idioms are oblivious, arrows are meticulous,
 * monads are promiscuous", Sam Lindley, Philip Wadler and Jeremy Yallop
 *
 * Their work is based on Paterson "A New Notation on Arrows"
 */
// Types A, B, C ::= · · · | A ~> B

// Constants:
// arr : (A → B) → (A ~> B)
// (>>>) : (A ~> B) → (B ~> C) → (A ~> C)
// first : (A ~> B) → (A×C ; B×C)

// Definitions
// second : (A ~> B) → (C×A ; C×B)
// (&&&) : (C ~> A) → (C ~> B) → (C ; A×B)
//  assoc ((a,b),c) = (a,(b,c))

// ~>1 : arr id >>> f = f
// ~>2 : f >>> arr id = f
// ~>3 : (f >>> g) >>> h = f >>> (g >>> h)
// ~>4 : arr(f.g) = arr(f) >>> arr (g)
// ~>5 : first(arr.f) = arr (f x id)
// ~>6 : first(f >>> g) = first(f) >>> first(g)
// ~>7 : first f >>> arr (id x g) = arr (id x g) >>> first f
// ~>8 : first f >>> arr fst = arr fst >>> f
// ~>9 : first(first f) >>> arr assoc = arr assoc >>> first f

// import zio.console._
import zio.test._
import zio.test.Assertion._
// import zio.test.environment._
import cats.arrow.Arrow
import cats.implicits._

import Helper._
import zio.random.Random

object LawsSpec
    extends DefaultRunnableSpec(
      suite("9 Arrow Base Laws Spec")(
        test("Law1: Left Identity") {
          val left  = arr(identity[Int]) >>> plusOne
          val right = plusOne
          assert(left(0), equalTo(right(0)))
        }
        // test("Law2: Right Identity") {
        //   val left  = plusOne >>> arr(identity[Int])
        //   val right = plusOne
        //   assert(left(0), equalTo(right(0)))
        // },
        // test("Law3: Associativity") {
        //   val left  = (plusOne >>> mulTwo) >>> minusOne
        //   val right = plusOne >>> (mulTwo >>> minusOne)
        //   assert(left(0), equalTo(right(0)))
        // },
        // test("Law4: Distributivity") {
        //   val left  = arr(plusOne >>> mulTwo)
        //   val right = arr(plusOne) >>> arr(mulTwo)
        //   assert(left(0), equalTo(right(0)))
        // },
        // test("Law5: ") {
        //   val left  = arr(plusOne)
        //   val right = ???
        //   assert(left(0), equalTo(right(0)))
        // },
        // test("Law6: ") {
        //   val left  = ???
        //   val right = ???
        //   assert(left(0), equalTo(right(0)))
        // },
        // test("Law7: ") {
        //   val left  = ???
        //   val right = ???
        //   assert(left(0), equalTo(right(0)))
        // },
        // test("Law8: ") {
        //   val left  = ???
        //   val right = ???
        //   assert(left(0), equalTo(right(0)))
        // },
        // test("Law9: ") {
        //   val left  = ???
        //   val right = ???
        //   assert(left(0), equalTo(right(0)))
        // }
      )
    )

object Helper {
  def arr[A, B](f: A => B) = Arrow[Function1].lift(f)

  // def plusOne(in: Int) = in + 1
  val plusOne  = (_: Int) + 1
  val minusOne = (_: Int) - 1
  val mulTwo   = (_: Int) * 2

  // def anyIntFunction(): Gen[Random, Int => Boolean] = Gen.function(Gen.boolean)
  def anyF(): Gen[Random, Int => Int] = Gen.function(Gen.anyInt)
}
