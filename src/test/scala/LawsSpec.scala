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

import zio.test._
import zio.test.Assertion._
import zio.test.check

import cats.arrow.Arrow
import cats.implicits._

import Helper._
import zio.random.Random

object LawsSpec
    extends DefaultRunnableSpec(
      suite("9 Arrow Base Laws Spec")(
        testM("Law1: Left Identity") {
          def left[A, B](f: A => B)  = arr(identity[A]) >>> f
          def right[A, B](f: A => B) = f

          check(int, anyF) { (i, f) =>
            {
              // val lef = left(f)(i)
              // val rig = right(f)(i)
              // println(s"i = ${i}, left = ${lef} right = ${rig}}")
              assert(left(f)(i), equalTo(right(f)(i)))
            }
          }
        },
        testM("Law2: Right Identity") {
          def left[A, B](f: A => B)  = f >>> arr(identity[B])
          def right[A, B](f: A => B) = f

          check(int, anyF) { (i, f) =>
            assert(left(f)(i), equalTo(right(f)(i)))
          }
        },
        testM("Law3: Associativity") {
          def left[A, B, C, D](f: A => B, g: B => C, h: C => D) = (f >>> g) >>> h

          def right[A, B, C, D](f: A => B, g: B => C, h: C => D) = f >>> (g >>> h)

          check(int, anyF, anyF, anyF) { (i, f, g, h) =>
            assert(left(f, g, h)(i), equalTo(right(f, g, h)(i)))
          }
        },
        testM("Law4: Distributivity") {
          def left[A, B, C](f: A => B, g: B => C) = arr(f >>> g)

          def right[A, B, C](f: A => B, g: B => C) = arr(f) >>> arr(g)

          check(int, anyF, anyF) { (i, f, g) =>
            assert(left(f, g)(i), equalTo(right(f, g)(i)))
          }
        },
        testM("Law5: First application") {
          def left[A, B, C](f: A => B): ((A, C)) => (B, C)  = arr(f).first[C]
          def right[A, B, C](f: A => B): ((C, A)) => (C, B) = arr(tensor(identity[C], f).tupled)

          check(int, anyF) { (i, f) =>
            val lhs = left(f)((i, 0))
            val rhs = right(f)((i, 0))
            // assert(left(f)((i, 0)), equalTo(right(f)((i, 0))))
            assert(lhs, equalTo(rhs.swap))
          }
        }
      )
    )

object Helper {
  def arr[A, B](f: A => B) = Arrow[Function1].lift(f)
  // def swap[A, B](data: (A, B)) = data.swap

  val ints = Gen.listOf(Gen.int(-10, 10))
  val int  = Gen.anyInt

  val anyF: Gen[Random, Int => Int] = Gen.function(Gen.anyInt)

  val add1: (Int => Int) = _ + 1

  def tensor[A, B, C, D](p: A => B, q: C => D): (A, C) => (B, D) = { (a, c) =>
    val b = p(a)
    val d = q(c)
    (b, d)
  }

}
