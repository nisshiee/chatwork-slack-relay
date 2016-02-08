package org.nisshiee.chatwork_lambda_test.domain.chatwork

import scala.concurrent.{ ExecutionContext, Future }

import com.github.nscala_time.time.Imports._
import org.nisshiee.chatwork_lambda_test.domain._

trait StreamService
extends UsesMessageRepository
with UsesLastLoadedRepository
with UsesCurrentTimeRepository {

  def messageStream(room: Room)(implicit ec: ExecutionContext): Future[List[Message]] = {
    val messagesF: Future[List[Message]] = messageRepository.latest(room)
    val lastLoadedF: Future[LastLoaded] = lastLoadedRepository.get(room)

    for {
      messages <- messagesF
      lastLoaded <- lastLoadedF
      (filtered, loaded) = filter(messages, lastLoaded)
      _ <- lastLoadedRepository.set(room, loaded)
    } yield filtered
  }

  private def filter(messages: List[Message], lastLoaded: LastLoaded): (List[Message], LastLoaded) = {
    val minTime = currentTimeRepository.get - 1.day
    val filtered = messages.
      filter { message =>
        message.sendTime >= minTime || message.updateTime >= minTime
      }.
      filter { message =>
        message.sendTime > lastLoaded.sendTime || message.updateTime > lastLoaded.updateTime
      }
    val loaded = filtered match {
      case Nil => lastLoaded
      case l   => LastLoaded(
        (lastLoaded.sendTime :: l.map(_.sendTime)).max,
        (lastLoaded.updateTime :: l.map(_.updateTime)).max)
    }

    (filtered, loaded)
  }
}

trait UsesStreamService {
  val streamService: StreamService
}
