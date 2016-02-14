package org.nisshiee.chatwork_slack_relay.infra.chatwork.serializers

import com.github.nscala_time.time.Imports._
import org.json4s._

class DateTimeSerializer extends CustomSerializer[DateTime](format =>
  ({
    case JInt(bigint) =>
      new DateTime(bigint.longValue * 1000, defaultTimeZone)
  }, {
    case dateTime: DateTime => JInt(dateTime.getMillis / 1000)
  }))
