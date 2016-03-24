package free

import free.Free.{Bind, Return}

import scalaz.effect.IO

object Example extends App {

  def runFreeIO(f: Free[IO, Unit]): Unit = f match {
    case Return(a)   => a
    case Bind(fi, k) => fi.map(i => runFreeIO(k(i))).unsafePerformIO
  }

  val interactToIO: NaturalTransformation[Interact, IO] = new NaturalTransformation[Interact, IO] {
    import scala.io.StdIn
    def apply[A](fa: Interact[A]): IO[A] = fa match {
      case Ask(prompt, f) => IO(f(StdIn.readLine(prompt)))
      case Tell(msg, a)   => IO(println(msg)).map(_ => a)
    }
  }

  import Interact._

  val program: API[Unit] = for {
    _    <- tell("Hey")
    name <- ask("What's your name ")
    _    <- tell(s"Your name is $name")
  } yield ()

  runFreeIO(program.compile(interactToIO))
}