package actor._old

import java.util.UUID

import akka.actor.Actor

class SimplePrinter extends Actor {

  val myId = UUID.randomUUID()

  override def preStart() = println(s"actor $myId is starting now... Ready to print received messages.")

  override def postStop() = println(s"actor $myId is being stopped now... Good bye.")

  override def receive = {
    case msg =>
      val response = s"actor $myId received: ${msg.toString}"
      println(response)
      sender() ! response
  }
}
