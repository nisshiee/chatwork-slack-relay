package org.nisshiee.chatwork_slack_relay.domain.slack

import monocle.macros.Lenses

@Lenses case class Post(
  username: String,
  iconUrl: String,
  author: Option[Author],
  body: String)
