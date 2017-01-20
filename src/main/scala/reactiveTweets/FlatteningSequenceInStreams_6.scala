package reactiveTweets

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

object FlatteningSequenceInStreams_6 extends App {
  import DataModel._
  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  // source
  val tweets: Source[Tweet, NotUsed] = Source.repeat(sampleTweet).take(5)
  val hashtags: Source[Hashtag, NotUsed] = tweets.mapConcat(_.hashtags.toList)
}
