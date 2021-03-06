package org.nisshiee.chatwork_slack_relay.domain

import scala.concurrent.{ ExecutionContext, Future }

import org.nisshiee.chatwork_slack_relay.domain.chatwork._
import org.nisshiee.chatwork_slack_relay.domain.slack._
import scalaz.OptionT
import scalaz.std.scalaFuture._
import scalaz.syntax.monad._

trait RelayService
extends UsesStreamService
with UsesTransferService
with UsesPostRepository
with UsesRoomRepository {
  def run(roomIds: List[Id[Room]])(implicit ec: ExecutionContext): Future[Unit] =
    roomIds.map(run(_)).foldLeft(Future.successful(())) { (a, e) =>
      a.flatMap { l => e }
    }

  def run(roomId: Id[Room])(implicit ec: ExecutionContext): Future[Unit] = (for {
    room <- OptionT.optionT(roomRepository.get(roomId))
    messages <- streamService.messageStream(room).liftM[OptionT]
    posts = messages.map { message => transferService.ctos(room, message) }
    _ <- postRepository.sendSequencially(posts).liftM[OptionT]
  } yield ()).run.map(_ => ())
}

trait UsesRelayService {
  val relayService: RelayService
}
