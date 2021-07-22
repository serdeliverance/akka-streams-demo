package io.github.serdeliverance.examples.part1

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}

object Example1 extends App {

  implicit val system           = ActorSystem("RedbeeExample")
  implicit val executionContext = system.dispatcher

  val graph = Source(1 to 100)
    .map(x => x + 1)
    .map(x => x * x * x)
    .map(e => s"num: $e")
    .to(Sink.foreach(println))

  graph.run()
}
