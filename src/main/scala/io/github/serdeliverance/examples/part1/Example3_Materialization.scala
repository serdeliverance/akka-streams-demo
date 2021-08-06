package io.github.serdeliverance.examples.part1

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, Keep, RunnableGraph, Sink, Source}
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

  // via example
  val combinedSource
    : Source[ByteString, NotUsed] = sourceList.via(convertToByteString) // materialize the value of sourceList which is of type NotUsed

  // via mat
  val combinedSource2 = sourceList.viaMat(addOne)((mat1, mat2) => mat2)

  // or the equivalent
  val combinedSource3 = sourceList.viaMat(addOne)(Keep.left)

  // to example
  val graph: NotUsed = combinedSource.to(sinkToFile).run() // materialize the left side value (the value of the source)

  // toMat
  val graph2: RunnableGraph[Future[IOResult]] = combinedSource.toMat(sinkToFile)(Keep.right) // we override this behaviour with toMat and choosing Keep.right
}
