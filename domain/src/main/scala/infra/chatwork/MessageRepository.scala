package org.nisshiee.chatwork_slack_relay.domain.chatwork

import scala.concurrent.{ ExecutionContext, Future }

trait MessageRepository {
  def latest(room: Room)(implicit ec: ExecutionContext): Future[List[Message]]
}

trait UsesMessageRepository {
  val messageRepository: MessageRepository
}
