package actor._old

import java.util.UUID

import akka.actor.Actor

class SimpleStateKeeper extends Actor {

  var myId = UUID.randomUUID().toString
  var messages: List[String] = List()

  override def receive = {
    case msg =>
      messages = msg.toString :: messages
      println(s"$myId: $messages")
  }
}

object SimpleStateKeeper {
  /*
   implement ExtractShardId  and  ExtractEntityId  here
   */
}
