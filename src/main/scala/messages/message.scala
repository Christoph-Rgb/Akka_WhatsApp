package messages

sealed trait Message
final case class CreateUserMessage(userId: String) extends Message
final case class RemoveUserMessage(userId: String) extends Message
final case class GetUsersMessage() extends Message
final case class ClearUsersMessage() extends Message


sealed trait Event
final case class CreatedUserEvent(userId: String) extends  Event
final case class RemovedUserEvent(userId: String) extends Event
final case class GetUsersEvent(users: Set[String]) extends Event
final case class ClearedUsersEvent() extends Event


sealed trait PersistenceEvent
final case class CreateUserPersistenceEvent(userId: String) extends PersistenceEvent
final case class RemoveUserPersistenceEvent(userId: String) extends PersistenceEvent
final case class ClearUsersPersistenceEvent() extends PersistenceEvent
