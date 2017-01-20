package stackOverflowQuickstart

import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.scaladsl.Tcp.IncomingConnection
import akka.stream.scaladsl.{Flow, Framing, RunnableGraph, Sink, Source, Tcp}
import akka.stream.{ActorMaterializer, Materializer, OverflowStrategy}
import akka.util.ByteString

import scala.concurrent.Future
import scala.util.{Failure, Success}

object FlowExamples extends App {

//  implicit val system = ActorSystem("Test1")
//  implicit val materializer = ActorMaterializer()

  val serverLogic: Flow[ByteString, ByteString, NotUsed] = {
    val delimiter: Flow[ByteString, ByteString, NotUsed] = Framing.delimiter(
      ByteString("\n"),
      maximumFrameLength = 256,
      allowTruncation = true
    )

    val receiver: Flow[ByteString, String, NotUsed] = Flow[ByteString].map { bytes =>
      val message = bytes.utf8String
      println(s"Server received: $message")
      message
    }

    val responder: Flow[String, ByteString, NotUsed] = Flow[String].map { message =>
      val answer = s"Server hereby responds to message: $message"
      ByteString(message)
    }

    Flow[ByteString]
      .via(delimiter)
      .via(receiver)
      .via(responder)
  }


  def mkServer(address: String, port: Int)
              (implicit system: ActorSystem, materializer: Materializer): Unit = {
    import system.dispatcher

    val incomingConnections: Source[Tcp.IncomingConnection, Future[Tcp.ServerBinding]] =
      Tcp().bind(address, port)

    val connectionHandler: Sink[IncomingConnection, Future[Done]] =
      Sink.foreach[Tcp.IncomingConnection] { conn =>
        println(s"Incomming connection from: ${conn.remoteAddress}")
        conn.handleWith(serverLogic)
      }

    val binding: Future[Tcp.ServerBinding] = incomingConnections to connectionHandler run

    binding onComplete {
      case Success(b) => println(s"Server started, listening on: ${b.localAddress}")
      case Failure(e) => println(s"Server could not be bound to $address:$port: ${e.getMessage}")
    }
  }
}