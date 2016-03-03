package org.nisshiee.chatwork_slack_relay.domain.slack

import scala.concurrent.{ ExecutionContext, Future }

trait PostRepository {
  def send(post: Post)(implicit ec: ExecutionContext): Future[Unit]

  def sendSequencially(posts: List[Post])(implicit ec: ExecutionContext): Future[Unit] =
    posts.foldLeft(Future.successful(())) { (f, post) => send(post) }
}

trait UsesPostRepository {
  val postRepository: PostRepository
}
