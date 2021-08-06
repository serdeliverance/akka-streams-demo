package io.github.serdeliverance.utils

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredDecoder, deriveConfiguredEncoder}
import io.circe.{Decoder, Encoder, Printer}
import io.github.serdeliverance.models.Transaction

trait JsonSupport {

  implicit val customConfig: Configuration =
    Configuration.default.withSnakeCaseMemberNames

  implicit val customPrinter: Printer =
    Printer.noSpaces.copy(dropNullValues = true)

  implicit val transactionEncoder: Encoder[Transaction] = deriveConfiguredEncoder[Transaction].mapJson(_.dropNullValues)
  implicit val transactionDecoder: Decoder[Transaction] = deriveConfiguredDecoder[Transaction]
}
