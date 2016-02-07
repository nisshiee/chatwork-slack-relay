package org.nisshiee.chatwork_lambda_test.domain.chatwork

import scala.concurrent.{ ExecutionContext, Future }

import com.github.nscala_time.time.Imports._
import org.nisshiee.chatwork_lambda_test.domain._

trait StreamService
extends UsesMessageRepository
with UsesCurrentTimeRepository {

  def messageStream(room: Room)(implicit ec: ExecutionContext): Future[List[Message]] = {
    val minTime = currentTimeRepository.get - 1.day
    messageRepository.latest(room).
      map { messages =>
        messages.filter { message =>
          message.sendTime >= minTime || message.updateTime >= minTime
        }
      }
  }
}

trait UsesStreamService {
  val streamService: StreamService
}
