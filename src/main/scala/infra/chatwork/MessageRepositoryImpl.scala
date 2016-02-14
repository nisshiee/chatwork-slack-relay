package org.nisshiee.chatwork_lambda_test.infra.chatwork

import scala.concurrent.ExecutionContext

import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.nisshiee.chatwork_lambda_test.domain._
import org.nisshiee.chatwork_lambda_test.domain.chatwork._
import org.nisshiee.chatwork_lambda_test.infra.chatwork.serializers._

object MessageRepositoryImpl extends MessageRepository {
  lazy val config = ConfigFactory.load
  lazy val token = config.as[String]("chatwork.token")
  implicit lazy val formats = DefaultFormats +
    new DateTimeSerializer +
    new IdSerializer[Message] +
    new IdSerializer[Account]

  override def latest(room: Room)(implicit ec: ExecutionContext) = {
    import dispatch._, Defaults._
    val req = url(s"https://api.chatwork.com/v1/rooms/${room.id.value}/messages").
      <:<(Map("X-ChatWorkToken" -> token)).
      <<?(Map("force" -> "1"))

    Http(req OK as.String).
      map(parse(_)).
      map(_.mapField {
        case ("message_id", v) => ("id", v)
        case ("account_id", v) => ("id", v)
        case f => f
      }).
      map { _.camelizeKeys.extract[List[Message]] }
  }
}

trait MixinMessageRepository extends UsesMessageRepository {
  override val messageRepository = MessageRepositoryImpl
}
