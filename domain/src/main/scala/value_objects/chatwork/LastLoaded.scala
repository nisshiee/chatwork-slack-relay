package org.nisshiee.chatwork_lambda_test.domain.chatwork

import monocle.macros.Lenses
import com.github.nscala_time.time.Imports._

@Lenses case class LastLoaded(
  sendTime: DateTime,
  updateTime: DateTime)

object LastLoaded {
  lazy val epoch = new DateTime(0L)
  lazy val default = LastLoaded(epoch, epoch)
}
