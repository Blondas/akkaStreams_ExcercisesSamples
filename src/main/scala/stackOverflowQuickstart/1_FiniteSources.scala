package stackOverflowQuickstart



import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

import scala.concurrent.Future

object FiniteSources extends App{

//  reactive streams are lazy and asynchronues => need to be evaluated explicitly by:
//    run*
//    runForeach works like foreach, but with explicitly asking for evaluation of the stream

  implicit val system = ActorSystem("Test1")
  // Akka actor system
  implicit val materializer = ActorMaterializer() // evaluation stream context => streams evaluated on top of actors
  import system.dispatcher
  // execution context of Futures

  val emptyS = Source.empty
  val singleS = Source.single("single element")
  val seqS = Source(1 to 3)
  val singleFutureS = Source.fromFuture(Future("single value from future"))

  singleFutureS runForeach println
}
