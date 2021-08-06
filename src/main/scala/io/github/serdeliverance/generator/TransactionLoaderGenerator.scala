package io.github.serdeliverance.generator

import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Keep, Source}
import akka.util.ByteString
import com.github.javafaker.Faker
import com.typesafe.config.ConfigFactory
import io.circe.syntax._
import io.github.serdeliverance.generator.TransactionFlow.transactionGenerator
import io.github.serdeliverance.utils.JsonSupport

import java.nio.file.Paths

object TransactionLoaderGenerator extends App with JsonSupport {

  implicit val system = ActorSystem("TransactionDataGenerator")
  implicit val ec     = system.dispatcher

  val config = ConfigFactory.load()

  val transactionCount = config.getInt("generator.transaction-count")

  val outputFile = Paths.get("transactions.txt")

  val transactionGeneratorFlow = transactionGenerator()

  val result = Source(1 to transactionCount)
    .via(transactionGeneratorFlow)
    .map(tx => tx.asJson.noSpaces)
    .map(line => ByteString(line + "\n"))
    .toMat(FileIO.toPath(outputFile))(Keep.right)
    .run()

  result.onComplete { _ =>
    system.log.info("Data generated")
    system.terminate()
  }

}
