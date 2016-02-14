package org.nisshiee.chatwork_slack_relay.domain.chatwork

import monocle.macros.Lenses
import org.nisshiee.chatwork_slack_relay.domain._

@Lenses case class Account(
  id: Id[Account],
  name: String,
  avatarImageUrl: String)
