package free

sealed trait Alg1[A]
case class Foo1(s: String) extends Alg1[Int]
case object Bar1 extends Alg1[Boolean]

sealed trait Alg2[A]
case class Foo2[A](s: String, f: String => A) extends Alg2[A]
case class Bar[A](f: Boolean => A) extends Alg2[A]


// Int, Char, String, case class Foo .. => Kind *
// List, Set, Future                    => Kind * -> *
// Map, Either                          => Kind * -> * -> *

