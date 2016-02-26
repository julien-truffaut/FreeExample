package free

sealed trait Interact[A]
case class Ask[A](prompt: String, f: String => A) extends Interact[A]
case class Tell[A](msg: String, a: A) extends Interact[A]


object Interact {
  implicit def functor: Functor[Interact] = new Functor[Interact] {
    def map[A, B](fa: Interact[A])(f: A => B): Interact[B] = fa match {
      case Ask(p, g)  => Ask(p, f compose g)
      case Tell(m, a) => Tell(m, f(a))
    }
  }

  type API[A] = Free[Interact, A]

  def tell(msg: String): API[Unit] =
    Free.lift(Tell(msg, ()))

  def ask(prompt: String): API[String] =
    Free.lift(Ask(prompt, identity))
}
