package org.nisshiee.chatwork_lambda_test.domain.chatwork

import scala.concurrent.{ ExecutionContext, Future }

trait LastLoadedRepository {
  def get(room: Room)(implicit ec: ExecutionContext): Future[LastLoaded]
  def set(room: Room, lastLoaded: LastLoaded)(implicit ec: ExecutionContext): Future[Unit]
}

trait UsesLastLoadedRepository {
  val lastLoadedRepository: LastLoadedRepository
}
