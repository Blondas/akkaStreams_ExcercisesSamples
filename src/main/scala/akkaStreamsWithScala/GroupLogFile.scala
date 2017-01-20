package akkaStreamsWithScala

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.util.matching.Regex

object GroupLogFile {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("Sys")
    implicit val materializer = ActorMaterializer()

    val LoglevelPattern: Regex = """.*\[(DEBUG|INFO|WARN|ERROR)\].*""".r
    val logFile = new File("src/main/resources/logfile.txt")

    FileIO.fromFile(logFile)
      .via(Framing.delimiter(ByteString(System.lineSeparator), maximumFrameLength = 512, allowTruncation = true))
      .map(_.utf8String)
      .map {
        case line@LoglevelPattern(level) => (level, line)
        case line@other => ("OTHER", line)
      }
      .groupBy(5, _._1) //
      .fold(("", List.empty[String])) {
        case ((_, list), (level, line)) => (level, line :: list)
      }.
      // write lines of each group to a separate file
      mapAsync(parallelism = 5) {
        case (level, groupList) =>
          Source(groupList.reverse).map(line => ByteString(line + "\n")).runWith(FileIO.toFile(new File(s"target/log-$level.txt")))
      }.
      mergeSubstreams.
      runWith(Sink.onComplete { _ =>
        system.shutdown()
      })
  }
}

/*
1. chunk into files
2. encode
3. regexp => tupple(level, line)
4. group by logLevel | rozdziela na mniejsze strumienie na podstawie logLevel
5.

 */
