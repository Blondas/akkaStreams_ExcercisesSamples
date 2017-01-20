package stackOverflowQuickstart

import akka.{Done, NotUsed}
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl.{RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}

import scala.concurrent.Future

object SinkInAction extends App {

  implicit val system = ActorSystem("Test1")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val source = Source(1 to 3)
  val sink = Sink.foreach[Int](e => println(s"sink received: $e"))
  val flow: RunnableGraph[NotUsed] = source to sink

  flow.run()
}
