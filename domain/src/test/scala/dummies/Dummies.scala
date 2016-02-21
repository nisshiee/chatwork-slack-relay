package org.nisshiee.chatwork_slack_relay.test

import org.nisshiee.chatwork_slack_relay.test.chatwork._
import org.nisshiee.chatwork_slack_relay.test.slack._

trait Dummies
extends Rooms
with    Messages
with    Posts {
  val unit: Unit = ()
}
