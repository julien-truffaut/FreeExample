package free

trait NaturalTransformation[F[_], G[_]]{
  def apply[A](fa: F[A]): G[A]
}

object NaturalTransformation {
  val listToOption = new NaturalTransformation[List, Option] {
    override def apply[A](fa: List[A]): Option[A] = fa.headOption
  }
}