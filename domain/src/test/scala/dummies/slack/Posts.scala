package org.nisshiee.chatwork_slack_relay.test.slack

import org.nisshiee.chatwork_slack_relay.domain.slack._

trait Posts {
  val post = Post(
    username   = "room name",
    iconUrl    = "icon path",
    author     = "name",
    authorLink = "https://www.chatwork.com/#!rid1-1",
    authorIcon = "http://avater.img/url",
    body       = "body")
}
