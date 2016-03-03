package org.nisshiee.chatwork_slack_relay.domain

import scala.concurrent.{ ExecutionContext, Future }

import org.nisshiee.chatwork_slack_relay.domain.chatwork._
import org.nisshiee.chatwork_slack_relay.domain.slack._
import scalaz.OptionT
import scalaz.std.scalaFuture._
import scalaz.syntax.monad._

trait AsakaiNotifyService
extends UsesStreamService
with    UsesPostRepository
with    UsesRoomRepository {
  def run(roomId: Id[Room], userName: String)(implicit ec: ExecutionContext): Future[Unit] = (for {
    room <- OptionT.optionT(roomRepository.get(roomId))
    messages <- streamService.messageStream(room).liftM[OptionT]
    asakaiPosts = messages.flatMap { m => extractAsakaiPost(room, m, userName) }
    _ <- postRepository.sendSequencially(asakaiPosts).liftM[OptionT]
  } yield ()).run.map(_ => ())

  def extractAsakaiPost(room: Room, message: Message, userName: String): Option[Post] =
    extractAsakaiLine(message, userName).
      map { line =>
        Post(
          username = room.name,
          iconUrl  = room.iconPath,
          author   = None,
          body     = line)
      }

  val AsakaiHeader = """＊＊＊＊＊　本日\([\d/]+\)のチームわけ　＊＊＊＊＊""".r
  def extractAsakaiLine(message: Message, userName: String): Option[String] = {
    val lines = message.body.split("""[\r\n]+""").map(_.trim).toList
    lines match {
      case AsakaiHeader() :: contents =>
        contents.find(_ contains userName)
      case _ => None
    }
  }
}

trait UsesAsakaiNotifyService {
  val asakaiNotifyService: AsakaiNotifyService
}
