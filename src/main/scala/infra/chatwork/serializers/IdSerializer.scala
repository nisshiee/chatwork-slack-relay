package org.nisshiee.chatwork_lambda_test.infra.chatwork.serializers

import scala.reflect.Manifest

import org.json4s._
import org.nisshiee.chatwork_lambda_test.domain.Id

class IdSerializer[A: Manifest] extends CustomSerializer[Id[A]](format =>
  ({
    case JInt(bigint) => Id[A](bigint.longValue)
  }, {
    case id: Id[_] => JInt(id.value)
  }))
