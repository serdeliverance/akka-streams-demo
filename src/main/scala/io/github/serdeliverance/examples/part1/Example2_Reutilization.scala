package io.github.serdeliverance.examples.part1

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString

import java.nio.file.Paths
import scala.concurrent.Future

/**
  * Example that shows component reutilization
  */
object Example2_Reutilization extends App {

  implicit val system = ActorSystem("Example2")

  // defining stream components separately which increase re-utilization
  val sourceList: Source[Int, NotUsed] = Source(1 to 1000)

  val addOne = Flow[Int].map(x => x + 1)

  val cube = Flow[Int].map(x => x * x * x)

  val toLine = Flow[Int].map(number => s"num: $number")

  val sinkPrintln = Sink.foreach(println)

  val graph1 = sourceList
    .via(addOne)
    .via(cube)
    .via(toLine)
    .to(sinkPrintln)

  graph1.run()

  // 2.1 defining different graphs re-using previous components

  val lineToBytes = Flow[String].map(line => ByteString(s"$line\n"))

  val sinkToFile = FileIO.toPath(Paths.get("result.txt"))

  val graph2 = sourceList
    .via(addOne)
    .via(cube)
    .via(toLine)
    .via(lineToBytes)
    .toMat(sinkToFile)(Keep.right)

  val result2: Future[IOResult] = graph2.run()

  // 2.3 more re-utilization

  // abstracting common patterns on new components

  val numberConverter = addOne.via(cube).via(toLine)

  val sinkStringToFile: Sink[String, NotUsed] = lineToBytes.to(sinkToFile)

  sourceList.via(numberConverter).to(sinkStringToFile).run()
}
