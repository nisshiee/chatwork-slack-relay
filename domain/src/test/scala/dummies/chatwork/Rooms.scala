package org.nisshiee.chatwork_slack_relay.test.chatwork

import org.nisshiee.chatwork_slack_relay.domain._
import org.nisshiee.chatwork_slack_relay.domain.chatwork._

trait Rooms {
  val roomId = Id[Room](1)
  val room   = Room(roomId, "room name", "icon path")
}
