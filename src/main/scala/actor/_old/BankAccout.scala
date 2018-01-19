package actor._old

import actor._old.BankingAccounts._
import akka.actor.Actor
import akka.cluster.sharding.ShardRegion

object BankingAccounts {
    sealed trait Transaction
    final case class CreateAccount(iban: String) extends Transaction
    final case class Deposit(iban: String, amount: Double) extends Transaction
    final case class Withdraw(iban: String, amount: Double) extends Transaction
    final case class ShowBalance(iban: String) extends Transaction

    val numberOfShards = 10

    def getShardIdFromMessage: ShardRegion.ExtractShardId = {
        case CreateAccount(iban) => (iban.hashCode() % numberOfShards).toString
        case Deposit(iban, _) => (iban.hashCode() % numberOfShards).toString
        case Withdraw(iban, _) => (iban.hashCode() % numberOfShards).toString
        case ShowBalance(iban) => (iban.hashCode() % numberOfShards).toString
    }

    def getActorIdFromMessage: ShardRegion.ExtractEntityId = {
        case msg @ CreateAccount(iban) => (iban, msg)
        case msg @ Deposit(iban, _) => (iban, msg)
        case msg @ Withdraw(iban, _) => (iban, msg)
        case msg @ ShowBalance(iban) => (iban, msg)
    }

}

class AccountActor extends Actor {

    var messages: List[Transaction] = List[Transaction]()
    var balance: Double = 0

    def initializedReceive(iban: String): Receive = {
        case msg @ Deposit(_, amount) =>
            this.balance = this.balance + amount
            this.messages = msg :: this.messages
        case msg @ Withdraw(_, amount) =>
            this.balance = this.balance - amount
            this.messages = msg :: this.messages
        case msg @ ShowBalance(_) =>
            this.messages = msg :: this.messages
            println(s"Balance $iban: $balance")
    }

    override def receive: Receive = {
        case msg @ CreateAccount(iban) =>
            context.become(this.initializedReceive(iban))
            println(s"created account $iban")
        case _ =>
            println("received unknown message")
    }

}