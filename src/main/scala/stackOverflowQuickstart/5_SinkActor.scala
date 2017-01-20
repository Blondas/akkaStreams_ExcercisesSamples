package stackOverflowQuickstart

import akka.actor.Actor.Receive
import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.scaladsl.{RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}


object SinkActor extends App {

  implicit val system = ActorSystem("Test1")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val actor = system.actorOf(Props(new Actor {
    override def receive = {
      case msg => println(s"actor received: $msg")
    }
  }))

  val source = Source(1 to 3)
  // Sends the elements of the stream to the given `ActorRef`.
  val sink = Sink.actorRef[Int](actor, onCompleteMessage = "stream compleated")
  val runnable: RunnableGraph[NotUsed] = source to sink

  runnable.run()
}
