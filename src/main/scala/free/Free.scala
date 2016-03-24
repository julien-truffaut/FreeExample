package free

/**
  * Some literature around Free Monad
  * - Reasonably priced  http://functionaltalks.org/2014/11/23/runar-oli-bjarnason-free-monad/
  * - Freer http://okmij.org/ftp/Haskell/extensible/more.pdf
  * - modern FP http://degoes.net/articles/modern-fp/
  * - cats Free http://typelevel.org/cats/tut/freemonad.html
  * - haskell Free https://hackage.haskell.org/package/free-4.12.4/docs/Control-Monad-Free.html
  * @tparam F
  * @tparam A
  */
sealed trait Free[F[_], A] { // F ~ List Option Future
  import Free.{Return, Bind}

  def map[B](f: A => B): Free[F, B] =
    flatMap(a => Return(f(a)))

  def flatMap[B](f: A => Free[F, B]): Free[F, B] =
    this match {
      case Return(a)   => f(a)
      case Bind(fi, k) => Bind(fi, (i: Any) => k(i).flatMap(f))
    }

  def compile[G[_]](nat: NaturalTransformation[F, G]): Free[G, A] =
    this match {
      case Return(a)   => Return(a)
      case Bind(fi, k) => Bind(nat.apply(fi), (i: Any) => k(i).compile(nat))
    }

  def foldMap[M[_]](f: NaturalTransformation[F, M])(implicit M: Monad[M]): M[A] = ???
}

object Free {

  case class Return[F[_], A](a: A) extends Free[F, A]
  case class Bind[F[_], I, A](fi: F[I], k: I => Free[F, A]) extends Free[F, A]

  def pure[F[_], A](a: A): Free[F, A] =
    Return(a)

  def lift[F[_],A](fa: F[A]): Free[F, A] =
    Bind(fa, (a: A) => Return(a))

  implicit def monadForFree[F[_]]: Monad[({type λ[α] = Free[F, α]})#λ] = // Free[F, ?]
    new Monad[({type λ[α] = Free[F, α]})#λ] {
      def flatMap[A, B](fa: Free[F, A])(f: A => Free[F, B]): Free[F, B] =
        fa.flatMap(f)

      def pure[A](a: A): Free[F, A] =
        Free.pure(a)
    }

}






