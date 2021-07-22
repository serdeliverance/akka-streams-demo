package io.github.serdeliverance.examples.part1

import akka.actor.ActorSystem
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.FileIO
import akka.util.ByteString
import io.github.serdeliverance.db.TransactionTable.transactionTable
import slick.jdbc.PostgresProfile.api._

import java.nio.file.Paths

object BatchClosure extends App {

  implicit val system  = ActorSystem("BatchClosure")
  implicit val session = SlickSession.forConfig("slick-postgres")

  val outputFile = Paths.get("output.txt")

  Slick
    .source(transactionTable.filter(t => t.email === "conception.hessel@gmail.com").result)
    .fold(BigDecimal(0))((acc, tx) => tx.amount + acc)
    .map(amount => ByteString(amount.toString()))
    .runWith(FileIO.toPath(outputFile))
}
