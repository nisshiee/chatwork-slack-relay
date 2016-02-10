package org.nisshiee.chatwork_lambda_test.domain

import scala.concurrent.{ ExecutionContext, Future }

import org.nisshiee.chatwork_lambda_test.domain.chatwork._
import org.nisshiee.chatwork_lambda_test.domain.slack._

trait RelayService
extends UsesStreamService
with UsesPostRepository {

  def run(room: Room)(implicit ex: ExecutionContext): Future[Unit] = for {
    messages <- streamService.messageStream(room)
    posts = messages.map(toSlackPost)
    _ <- sendSequencially(posts)
  } yield ()

  def toSlackPost(message: Message) = Post(
    message.account.name,
    message.account.avatarImageUrl,
    message.body)

  def sendSequencially(posts: List[Post])(implicit ec: ExecutionContext): Future[Unit] =
    posts.foldLeft(Future.successful(())) { (f, post) =>
      postRepository.send(post)
    }
}

trait UsesRelayService {
  val relayService: RelayService
}
