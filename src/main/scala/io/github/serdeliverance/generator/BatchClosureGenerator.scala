package io.github.serdeliverance.generator

import akka.actor.ActorSystem
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.Source
import com.typesafe.config.ConfigFactory
import io.github.serdeliverance.db.TransactionTable.transactionTable
import io.github.serdeliverance.generator.TransactionFlow.transactionGenerator
import slick.jdbc.PostgresProfile.api._

object BatchClosureGenerator extends App {

  implicit val system = ActorSystem("BatchClosureDataGenerator")
  implicit val ec     = system.dispatcher

  implicit val session = SlickSession.forConfig("slick-postgres")

  val config = ConfigFactory.load()

  val transactionCount = config.getInt("generator.transaction-count")

  val transactionGeneratorFlow = transactionGenerator(true)

  val result = Source(1 to transactionCount)
    .via(transactionGeneratorFlow)
    .runWith(Slick.sink(tx => transactionTable += tx))

  result.onComplete { _ =>
    system.log.info("Data generated")
    system.terminate()
    session.close()
  }
}
