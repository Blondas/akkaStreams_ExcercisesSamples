package stackOverflowQuickstart

import akka.actor.Actor.Receive
import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.scaladsl.{Flow, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}

import scala.concurrent.Future


object FlowExamples extends App {

  implicit val system = ActorSystem("Test1")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val actor = system.actorOf(Props(new Actor {
    override def receive = {
      case msg => println(s"actor received: $msg")
    }
  }))

  val source = Source(1 to 3)
  val sink: Sink[Any, Future[Done]] = Sink.foreach(println)

  val invert: Flow[Int, Int, NotUsed] = Flow[Int].map(_ * -1)
  val doubler: Flow[Int, Double, NotUsed] = Flow[Int].map(_ * 0.2)

  val runnable = source via invert via doubler to sink

  runnable.run()
}
