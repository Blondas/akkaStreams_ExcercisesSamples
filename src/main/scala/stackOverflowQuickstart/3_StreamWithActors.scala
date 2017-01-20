package stackOverflowQuickstart

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.Source

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

object StreamWithActors extends App {

  implicit val system = ActorSystem("Test1")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  def run(actor: ActorRef): Future[Unit] = {
    Future {
      Thread.sleep(300)
      actor ! 1
    }
    Future {
      Thread.sleep(200)
      actor ! 2
    }
    Future {
      Thread.sleep(100)
      actor ! 3
    }
  }

  // Creates a `Source` that is materialized as an [[akka.actor.ActorRef]]
  val s = Source
    .actorRef[Int](bufferSize = 0, overflowStrategy = OverflowStrategy.fail)
    .mapMaterializedValue(run)

  s runForeach println
}
