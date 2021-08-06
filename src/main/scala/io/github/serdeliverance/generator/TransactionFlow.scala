package io.github.serdeliverance.generator

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.github.javafaker.{CreditCardType, Faker}
import com.typesafe.config.ConfigFactory
import io.github.serdeliverance.models.Transaction
import io.github.serdeliverance.utils.{EncryptionUtils, RandomUtils}

import java.time.LocalDateTime

object TransactionFlow extends EncryptionUtils with RandomUtils {

  private lazy val config = ConfigFactory.load()
  private lazy val key    = config.getString("encryption.key")

  private lazy val faker = new Faker()

  def transactionGenerator(withEncryption: Boolean = false): Flow[Any, Transaction, NotUsed] =
    Flow[Any].map(_ => {
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
        cardNumber = if (withEncryption) encrypt(key, cardNumber) else cardNumber,
        dateTime = LocalDateTime.now().toString,
        holder = holder,
        installments = installments,
        cardType = cardType.name(),
        email = email,
        status = status
      )
    })
}
