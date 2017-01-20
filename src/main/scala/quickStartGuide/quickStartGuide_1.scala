package quickStartGuide

import akka.stream._
import akka.stream.scaladsl._
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.util.ByteString
import java.nio.file.Paths

import scala.concurrent.Future

object quickStartGuide_1 extends App{
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  // [TypeOfEmittedElement, someAuxiliaryValue]
  val source: Source[Int, NotUsed] = Source(1 to 100)

  val result: Future[Done] = source.runForeach(println(_))
}
