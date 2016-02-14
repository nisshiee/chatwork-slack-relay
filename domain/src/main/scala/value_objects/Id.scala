package org.nisshiee.chatwork_slack_relay.domain

case class Id[A](value: Long) extends AnyVal {
  override def toString: String = value.toString
}
