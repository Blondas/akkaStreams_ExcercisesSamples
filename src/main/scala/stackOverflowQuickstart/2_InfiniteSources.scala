package stackOverflowQuickstart



import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

import scala.concurrent.Future

object InfiniteSources extends App {

  implicit val system = ActorSystem("Test1")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val s = Source.repeat(5)
  s take 3 runForeach println
}
