package org.nisshiee.chatwork_lambda_test.domain.chatwork

import org.nisshiee.chatwork_lambda_test.domain._

case class Account(
  id: Id[Account],
  name: String,
  avatarImageUrl: String)
