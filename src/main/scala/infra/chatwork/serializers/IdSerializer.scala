package org.nisshiee.chatwork_slack_relay.infra.chatwork.serializers

import scala.reflect.Manifest

import org.json4s._
import org.nisshiee.chatwork_slack_relay.domain.Id

class IdSerializer[A: Manifest] extends CustomSerializer[Id[A]](format =>
  ({
    case JInt(bigint) => Id[A](bigint.longValue)
  }, {
    case id: Id[_] => JInt(id.value)
  }))
