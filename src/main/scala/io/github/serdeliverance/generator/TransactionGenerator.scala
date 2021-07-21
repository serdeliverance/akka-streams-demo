package io.github.serdeliverance.generator

import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Keep, Source}
import akka.util.ByteString
import com.github.javafaker.{CreditCardType, Faker}
import com.typesafe.config.ConfigFactory
import io.circe.syntax._
import io.github.serdeliverance.models.Transaction
import io.github.serdeliverance.utils.{JsonSupport, RandomUtils}

import java.nio.file.Paths
import java.time.LocalDateTime

object TransactionGenerator extends App with JsonSupport with RandomUtils {

  implicit val system = ActorSystem("TransactionDataGenerator")
  implicit val ec     = system.dispatcher

  val config = ConfigFactory.load()

  val transactionCount = config.getInt("generator.transaction-count")

  val faker = new Faker()

  val outputFile = Paths.get("transactions.txt")

  val result = Source(1 to transactionCount)
    .map(_ => {
      val cardType     = CreditCardType.values()(randomIntInRage(0, CreditCardType.values().size - 1))
      val cardNumber   = faker.finance().creditCard(cardType)
      val user         = faker.name()
      val holder       = user.fullName()
      val email        = s"${user.username()}@gmail.com"
      val amount       = randomIntInRage(1, 9999)
      val installments = randomIntInRage(1, 12)
      val status       = randomState()
      Transaction(
        id = None,
        amount = amount,
        cardNumber = cardNumber,
        dateTime = LocalDateTime.now().toString,
        holder = holder,
        installments = installments,
        cardType = cardType.name(),
        email = email,
        status = status
      )
    })
    .map(tx => tx.asJson.noSpaces)
    .map(line => ByteString(line + "\n"))
    .toMat(FileIO.toPath(outputFile))(Keep.right)
    .run()

  result.onComplete { _ =>
    system.log.info("Data generated")
    system.terminate()
  }

}
