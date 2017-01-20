package reactiveTweets

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future


object TransformingConsumingSimpleStreams_5 extends App {
  import DataModel._
  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  // source
  val tweets: Source[Tweet, NotUsed] = Source.repeat(sampleTweet).take(5)

  val authors: Source[Author, NotUsed] = tweets
    .filter(_.hashtags.contains(akkaTag))
    .map(_.author)

  val a: Future[Done] = authors.runWith(Sink.foreach(println))
//  authors.runForeach(println)
}
