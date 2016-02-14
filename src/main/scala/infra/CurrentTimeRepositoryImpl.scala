package org.nisshiee.chatwork_slack_relay.infra

import com.github.nscala_time.time.Imports._
import org.nisshiee.chatwork_slack_relay.domain._

object CurrentTimeRepositoryImpl extends CurrentTimeRepository {
  override lazy val get = DateTime.now
}

trait MixinCurrentTimeRepository extends UsesCurrentTimeRepository {
  override val currentTimeRepository = CurrentTimeRepositoryImpl
}
