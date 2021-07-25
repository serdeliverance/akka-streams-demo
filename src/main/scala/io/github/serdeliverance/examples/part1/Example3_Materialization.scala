package io.github.serdeliverance.examples.part1

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, Sink, Source}
import akka.util.ByteString

import java.nio.file.Paths
import scala.concurrent.Future

/**
  * Example that shows how to materialize different values emitted by graph components
  */
object Example3_Materialization extends App {

  implicit val system = ActorSystem()

  val sourceList: Source[Int, NotUsed] = Source(1 to 1000) // emits no value to the external world

  val addOne: Flow[Int, Int, NotUsed] = Flow[Int].map(x => x + 1) // emits no value to the external world

  val convertToByteString
    : Flow[Int, ByteString, NotUsed] = Flow[Int].map(x => ByteString(x)) // emits no value to the external world

  val sinkToFile: Sink[ByteString, Future[IOResult]] = FileIO.toPath(Paths.get("result.txt")) // emits a file

  val result1: NotUsed = sourceList
    .via(addOne)
    .via(convertToByteString)
    .to(sinkToFile)
    .run()

  val sinkIgnore = Sink.ignore

  val source: Source[Int, NotUsed] = Source(1 to 1000)

  val result: NotUsed = sinkIgnore.runWith(source)
}
