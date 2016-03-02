package org.nisshiee.chatwork_slack_relay.domain.slack

import monocle.macros.Lenses

@Lenses case class Author(
  name: String,
  link: String,
  icon: String)
