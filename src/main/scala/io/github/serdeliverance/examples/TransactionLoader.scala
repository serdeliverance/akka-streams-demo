package io.github.serdeliverance.examples

import akka.actor.ActorSystem
import akka.stream.alpakka.slick.scaladsl.{Slick, SlickSession}
import akka.stream.scaladsl.{FileIO, Framing}
import akka.util.ByteString
import com.typesafe.config.ConfigFactory
import io.circe.parser.decode
import io.github.serdeliverance.db.TransactionTable.transactionTable
import io.github.serdeliverance.models.Transaction
import io.github.serdeliverance.utils.{EncryptionUtils, JsonSupport}

import java.nio.file.Paths
import slick.jdbc.PostgresProfile.api._

object TransactionLoader extends App with JsonSupport with EncryptionUtils {

  val config = ConfigFactory.load()
  val key    = config.getString("encryption.key")

  implicit val system = ActorSystem("TransactionLoader")

  // not required by the stream itselft but for flatMapping over `Future`
  implicit val ec = system.dispatcher

  val file = Paths.get("transactions.txt")

  implicit val session = SlickSession.forConfig("slick-postgres")

  val result = FileIO
    .fromPath(file)
    .via(Framing.delimiter(ByteString(System.lineSeparator()), 256, true))
    .map(_.utf8String)
    .map(decode[Transaction](_))
    .collect {
      case Right(parsedTx) => parsedTx
    }
    .map(tx => tx.copy(cardNumber = encrypt(key, tx.cardNumber)))
    .runWith(Slick.sink(tx => transactionTable += tx))

  // shutdown the actor system when finished
  result.onComplete { _ =>
    system.log.info("Shutdown actor system")
    system.terminate()
  }

  // hook for closing slick session
  system.registerOnTermination {
    system.log.info(s"Closing slick session")
    session.close()
  }
}
