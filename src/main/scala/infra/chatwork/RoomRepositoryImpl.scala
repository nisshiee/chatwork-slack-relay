package org.nisshiee.chatwork_slack_relay.infra.chatwork

import scala.concurrent.ExecutionContext

import com.typesafe.config.ConfigFactory
import dispatch._, Defaults._
import net.ceedubs.ficus.Ficus._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.nisshiee.chatwork_slack_relay.domain._
import org.nisshiee.chatwork_slack_relay.domain.chatwork._


object RoomRepositoryImpl extends RoomRepository {
  lazy val config = ConfigFactory.load
  lazy val token = config.as[String]("chatwork.token")

  override def get(id: Id[Room])(implicit ec: ExecutionContext) = {
    val req = url(s"https://api.chatwork.com/v1/rooms/${id}").
      <:<(Map("X-ChatWorkToken" -> token))

    Http(req OK as.String).
      map(parse(_)).
      map { json =>
        (json \ "name", json \ "icon_path") match {
          case (JString(name), JString(iconPath)) => Some(Room(id, name, iconPath))
          case _ => {
            println(json)
            None
          }
        }
      }.
      recover { case e => {
        e.printStackTrace
        None
      }}
  }
}

trait MixinRoomRepository extends UsesRoomRepository {
  override val roomRepository = RoomRepositoryImpl
}
