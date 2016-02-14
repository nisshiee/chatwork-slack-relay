package org.nisshiee.chatwork_slack_relay.domain.slack

case class Post(
  username: String,
  iconUrl: String,
  author: String,
  authorIcon: String,
  body: String)
