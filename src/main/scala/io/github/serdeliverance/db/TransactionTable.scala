package io.github.serdeliverance.db

import io.github.serdeliverance.models.Transaction
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

class TransactionTable(tag: Tag) extends Table[Transaction](tag, "transactions") {

  def id: Rep[Option[Long]]   = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def amount: Rep[BigDecimal] = column[BigDecimal]("amount")
  def cardNumber: Rep[String] = column[String]("card_number")
  def dateTime: Rep[String]   = column[String]("date_time")
  def holder: Rep[String]     = column[String]("holder")
  def installments: Rep[Int]  = column[Int]("installments")
  def cardType: Rep[String]   = column[String]("card_type")
  def email: Rep[String]      = column[String]("email")
  def status: Rep[String]     = column[String]("status")

  def * : ProvenShape[Transaction] =
    (id, amount, cardNumber, dateTime, holder, installments, cardType, status, email).mapTo[Transaction]
}

object TransactionTable {
  lazy val transactionTable = TableQuery[TransactionTable]
}
