package org.nisshiee.chatwork_slack_relay.test.chatwork

import org.nisshiee.chatwork_slack_relay.domain._
import org.nisshiee.chatwork_slack_relay.domain.chatwork._

trait Accounts {
  val account    = Account(
    Id[Account](1L),
    "name",
    "http://avater.img/url")
}
