package org.nisshiee.chatwork_lambda_test.domain.slack

import scala.concurrent.{ ExecutionContext, Future }

trait PostRepository {
  def send(post: Post)(implicit ec: ExecutionContext): Future[Unit]
}

trait UsesPostRepository {
  val postRepository: PostRepository
}
