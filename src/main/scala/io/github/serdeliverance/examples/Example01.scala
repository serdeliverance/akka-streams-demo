package io.github.serdeliverance.examples

import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString

import java.nio.file.Paths

object Example01 extends App {

  implicit val system           = ActorSystem("Quickstart")
  implicit val executionContext = system.dispatcher

  // 1 - our first stream

  val result = Source(1 to 100)
    .map(e => e * 2)
    .map(e => s"num: $e")
    .runWith(Sink.foreach(println))

  // when completed, we shout down the actor system in order to finish our program
  // result.onComplete(_ => system.terminate())

  // 2 - reutilizable components

  val source = Source(1 to 100)

  val multiply2 = Flow[Int].map(_ * 2)

  val convertToString = Flow[Int].map(e => s"num: $e")

  val convertToByteString = Flow[String].map(string => ByteString(string + "\n"))

  val fileSink = FileIO.toPath(Paths.get("result.txt"))

  val printlnSink = Sink.foreach(println)

  val graph = source.via(multiply2).via(convertToString).via(convertToByteString).toMat(fileSink)(Keep.right)

  val result2 = graph.run()

  result2.onComplete(_ => system.terminate())
}
