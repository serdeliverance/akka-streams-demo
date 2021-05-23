package io.github.serdeliverance.playground

import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import akka.{Done, NotUsed}

import java.nio.file.Paths
import scala.concurrent.Future

object Example01 extends App {

  implicit val system: ActorSystem = ActorSystem("Quickstart")

  val source: Source[Int, NotUsed] = Source(1 to 100)

  val done: Future[Done] = source.runForeach(i => println(i))

  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  val result: Future[IOResult] =
    factorials.map(num => ByteString(s"$num\n")).runWith(FileIO.toPath(Paths.get("factorials.txt")))
}
