package actor._old

import akka.actor.Actor

class SimpleResponder(name: String) extends Actor {

  override def receive = {
    case str:String =>
      val response = s"SimpleResponder '$name' received string: $str"
      println(response)
      sender() ! response
    case msg =>
      val response = s"SimpleResponder '$name' received message of unknown type: ${msg.toString}"
      println(response)
      sender() ! response
  }

}
