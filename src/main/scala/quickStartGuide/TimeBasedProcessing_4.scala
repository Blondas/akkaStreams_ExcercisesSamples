package quickStartGuide

import java.nio.file.Paths

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.Future
import scala.concurrent.duration._

object TimeBasedProcessing_4 extends App {
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  val factorials: Source[BigInt, NotUsed] =
    Source(1 to 100)
      .scan(BigInt(1))((acc, next) => acc * next)

  val indexes: Source[Int, NotUsed] = Source(0 to 100)

  val done: Future[Done] =
    factorials
      .zipWith(indexes)((num, idx) => s"$idx! = $num")
      .throttle(1, 1.second, 1, ThrottleMode.shaping)
      .runForeach(println)
}
