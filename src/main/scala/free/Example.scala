package free

import scalaz.concurrent.Task

object Example extends App {

  def runFreeTask(f: Free[Task, Unit]): Unit = f match {
    case Return(a)   => a
    case Bind(fi, k) => fi.map(i => runFreeTask(k(i))).run
  }

  val interactToTask: NaturalTransformation[Interact, Task] = new NaturalTransformation[Interact, Task] {
    import scala.io.StdIn
    def apply[A](fa: Interact[A]): Task[A] = fa match {
      case Ask(prompt, f) => Task.delay(f(StdIn.readLine(prompt)))
      case Tell(msg, a)   => Task.delay{
        println(msg)
        a
      }
    }
  }

  import Interact._

  val program: API[Unit] = for {
    _    <- tell("Hey")
    name <- ask("What's your name")
    _    <- tell(s"Your name is $name")
  } yield ()

  runFreeTask(program.transform(interactToTask))
}