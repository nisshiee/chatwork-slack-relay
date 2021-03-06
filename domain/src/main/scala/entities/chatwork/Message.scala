package org.nisshiee.chatwork_slack_relay.domain.chatwork

import com.github.nscala_time.time.Imports._
import monocle.macros.Lenses
import org.nisshiee.chatwork_slack_relay.domain._

@Lenses case class Message(
  id: Id[Message],
  account: Account,
  body: String,
  sendTime: DateTime,
  updateTime: DateTime)
