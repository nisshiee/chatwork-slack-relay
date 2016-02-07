package org.nisshiee.chatwork_lambda_test.domain.chatwork

import scala.concurrent.{ ExecutionContext, Future }

trait MessageRepository {
  def latest(room: Room)(implicit ec: ExecutionContext): Future[List[Message]]
}

trait UsesMessageRepository {
  val messageRepository: MessageRepository
}
