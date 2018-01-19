package app

import actor.UserManagerActor
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import messages._
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

//import akka.http.scaladsl.server.Directives.Segment
//import akka.http.scaladsl.server.Directives.pathPrefix
//import akka.http.scaladsl.server.Directives.path
//import akka.http.scaladsl.server.Directives.post
//import akka.http.scaladsl.server.Directives.get
//import akka.http.scaladsl.server.Directives.delete
//import akka.http.scaladsl.server.Directives.onComplete
//import akka.http.scaladsl.server.Directives.complete

object Start extends App with DefaultJsonProtocol with SprayJsonSupport{

  implicit val createdUserEventFormat = jsonFormat1(CreatedUserEvent)
  implicit val removedUserEventFormat = jsonFormat1(RemovedUserEvent)
  implicit val getUsersEventFormat = jsonFormat1(GetUsersEvent)


  println(s"starting up...")

  implicit val system = ActorSystem("whats-app-system")
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  val userManager = system.actorOf(Props(classOf[UserManagerActor], "user-manager-actor"), "user-manager-actor")

  val port = 8087
  val host = "localhost"
  val paths: Flow[HttpRequest, HttpResponse, Any] =
    pathPrefix("api" / "v1") {
      path("user" / Segment) { userId =>
        post {
          onComplete((userManager ? CreateUserMessage(userId)).mapTo[CreatedUserEvent]) {
            case Success(createdUserEvent) =>
              complete(201, createdUserEvent)
            case Failure(exception) =>
              complete(400, exception.getMessage())
          }
        }
      } ~
      path("user" / Segment) { userId =>
        delete {
          onComplete((userManager ? RemoveUserMessage(userId)).mapTo[RemovedUserEvent]) {
            case Success(removedUserEvent) =>
              complete(200, removedUserEvent)
            case Failure(exception) =>
              complete(400, exception.getMessage())
          }
        }
      }~
      path("user") {
        get {
          onComplete((userManager ? GetUsersMessage()).mapTo[GetUsersEvent]) {
            case Success(getUsersEvent) =>
              complete(getUsersEvent)
            case Failure(exception) =>
              complete(400, exception.getMessage())
          }
        }
      }~
      path("user") {
        delete {
          onComplete((userManager ? ClearUsersMessage()).mapTo[ClearedUsersEvent]) {
            case Success(clearedUsersEvent) =>
              complete("deleted")
            case Failure(exception) =>
              complete(400, exception.getMessage())
          }
        }
      }
    }

  val webserverHandle : Future[Http.ServerBinding] = Http().bindAndHandle(paths, host, port)

//  userManager ! CreateUserMessage("christoph")
//  userManager ! CreateUserMessage("hansi")
//  (userManager ? GetUsersMessage()).mapTo[GetUsersEvent].onComplete({
//    case Success(getUsersEvent) =>
//      println(s"GetUsersMessage: ${getUsersEvent.users}")
//    case Failure(exception) =>
//      println(s"GetUsersMessage: ${exception.getMessage()}")
//  })
//  userManager ! RemoveUserMessage("hansi")
//  (userManager ? GetUsersMessage()).mapTo[GetUsersEvent].onComplete({
//    case Success(getUsersEvent) =>
//      println(s"GetUsersMessage: ${getUsersEvent.users}")
//    case Failure(exception) =>
//      println(s"GetUsersMessage: ${exception.getMessage()}")
//  })
//
//  userManager ! ClearUsersMessage()


//
//
// val accountsActorRef: ActorRef = ClusterSharding(system).start(
//   typeName = "bakning-accounts-shard",
//   entityProps = Props[AccountActor],
//   settings = ClusterShardingSettings(system),
//   extractEntityId = BankingAccounts.getActorIdFromMessage,
//   extractShardId = BankingAccounts.getShardIdFromMessage
// )


  println("stop by hitting ctrl+c")
}
