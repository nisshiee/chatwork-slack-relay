package org.nisshiee.chatwork_slack_relay.test.chatwork

import com.github.nscala_time.time.Imports._
import org.nisshiee.chatwork_slack_relay.domain._
import org.nisshiee.chatwork_slack_relay.domain.chatwork._

trait Messages
extends Accounts {
  val message    = Message(
    Id[Message](1L),
    account,
    "body",
    new DateTime(2016, 2, 10, 12, 33, 44),
    new DateTime(0L))
}
