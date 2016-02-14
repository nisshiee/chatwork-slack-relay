package org.nisshiee.chatwork_slack_relay.domain

import monocle.macros.Lenses

@Lenses case class Id[A](value: Long) extends AnyVal {
  override def toString: String = value.toString
}
