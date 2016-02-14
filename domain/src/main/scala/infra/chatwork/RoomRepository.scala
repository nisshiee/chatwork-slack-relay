package org.nisshiee.chatwork_slack_relay.domain.chatwork

import scala.concurrent.{ ExecutionContext, Future }

import org.nisshiee.chatwork_slack_relay.domain._

trait RoomRepository {
  def get(id: Id[Room])(implicit ec: ExecutionContext): Future[Option[Room]]
}

trait UsesRoomRepository {
  val roomRepository: RoomRepository
}
