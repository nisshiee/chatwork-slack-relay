package org.nisshiee.chatwork_lambda_test.domain.chatwork

import com.github.nscala_time.time.Imports._
import org.nisshiee.chatwork_lambda_test.domain._

case class Message(
  id: Id[Message],
  account: Account,
  body: String,
  sendTime: DateTime,
  updateTime: DateTime)
