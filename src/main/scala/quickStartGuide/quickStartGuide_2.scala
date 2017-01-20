package quickStartGuide

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.Future
import scala.util.Success

object quickStartGuide_2 extends App{
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 4)
  val factorials: Source[BigInt, NotUsed] = source
//    .map(e => {print(s"element: $e => "); e})
    .scan(BigInt(1))((acc, next) => acc * next)

  val result: Future[IOResult] = factorials
    .map(num => ByteString(s"$num\n"))
      .runWith(FileIO.toPath((Paths.get("factorials.txt"))))
//        .runForeach(e => println(s"silnia: $e"))


}
