package actor

import akka.persistence.{PersistentActor, SnapshotOffer}
import messages._

class UserManagerActor(id: String) extends PersistentActor{

  override def persistenceId = id

  var userDirectory: UserDirectory = UserDirectory(Set())

  private def handleReceive: PersistenceEvent => Unit = {
    case CreateUserPersistenceEvent(userId) =>
        userDirectory = userDirectory.copy(userDirectory.users + userId)
        sender() ! CreatedUserEvent(userId)
    case RemoveUserPersistenceEvent(userId) =>
        userDirectory = userDirectory.copy(userDirectory.users - userId)
        sender() ! RemovedUserEvent(userId)
    case ClearUsersPersistenceEvent() =>
        userDirectory = userDirectory.copy(Set())
        saveSnapshot(userDirectory)
        sender() ! ClearedUsersEvent()
    case _ =>
      println("UserManager: Received unkown persistence event")
  }


  override def receiveRecover = {
    case persistenceEvent : PersistenceEvent =>
      handleReceive(persistenceEvent)
    case snapshotOffer @ SnapshotOffer(metadata, savedUserDirectory: UserDirectory) =>
      userDirectory = userDirectory.copy(savedUserDirectory.users)
    case event =>
      println("UserManager tried to recover from unkown event")
  }

  override def receiveCommand = {
    case CreateUserMessage(userId) =>
      println(s"UserManager Create: $userId")
      persist(CreateUserPersistenceEvent(userId))(event => handleReceive(event))
    case RemoveUserMessage(userId) =>
      println(s"UserManager Remove: $userId")
      persist(RemoveUserPersistenceEvent(userId))(event => handleReceive(event))
    case GetUsersMessage() =>
      println(s"UserManager GetUsers")
      sender() ! GetUsersEvent(userDirectory.users)
    case ClearUsersMessage() =>
      println(s"UserManager ClearUsers")
      handleReceive(ClearUsersPersistenceEvent())
    case _ =>
      sender() ! "UserManager received unkown message"
  }

}

final case class UserDirectory(users: Set[String])
