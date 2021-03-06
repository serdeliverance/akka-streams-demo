package io.github.serdeliverance.examples.part1

import akka.actor.ActorSystem
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.FileIO
import akka.util.ByteString
import io.github.serdeliverance.db.TransactionTable.transactionTable
import slick.jdbc.PostgresProfile.api._

import java.nio.file.Paths

/**
  * BatchClosure example 2
  *
  * Before running this sample, be sure to have all dockers up (docker-compose up) and to have run batchclosureGenerator.sh
  */
object Example5_BatchClosureAgain extends App {

  implicit val system  = ActorSystem("BatchClosure")
  implicit val ec      = system.dispatcher
  implicit val session = SlickSession.forConfig("slick-postgres")

  private val EMPTY_CARD_TYPE = ""

  val brandBalanceOutputFile = Paths.get("brand-balance.txt")

  val result = Slick
    .source(transactionTable.filter(_.status === "approved").result)
    .groupBy(20, _.cardType)
    .fold((EMPTY_CARD_TYPE, BigDecimal(0)))((acc, tx) => (tx.cardType, tx.amount + acc._2))
    .mergeSubstreams
    .map(cardTypeAndTotal => s"cardType: ${cardTypeAndTotal._1}, total: ${cardTypeAndTotal._2}")
    .map(line => ByteString(line + System.lineSeparator()))
    .runWith(FileIO.toPath(brandBalanceOutputFile))

  result.onComplete { _ =>
    system.terminate()
    session.close()
  }
}
