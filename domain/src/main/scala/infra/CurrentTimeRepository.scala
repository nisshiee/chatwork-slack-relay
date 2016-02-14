package org.nisshiee.chatwork_slack_relay.domain

import com.github.nscala_time.time.Imports._

trait CurrentTimeRepository {
  def get: DateTime
}

trait UsesCurrentTimeRepository {
  val currentTimeRepository: CurrentTimeRepository
}
