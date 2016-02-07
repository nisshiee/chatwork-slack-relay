package org.nisshiee.chatwork_lambda_test.domain

case class Id[A](value: Long) extends AnyVal {
  override def toString: String = value.toString
}
