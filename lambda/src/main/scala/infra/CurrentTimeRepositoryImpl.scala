package org.nisshiee.chatwork_lambda_test.infra

import com.github.nscala_time.time.Imports._
import org.nisshiee.chatwork_lambda_test.domain._

object CurrentTimeRepositoryImpl extends CurrentTimeRepository {
  override lazy val get = DateTime.now
}

trait MixinCurrentTimeRepository extends UsesCurrentTimeRepository {
  override val currentTimeRepository = CurrentTimeRepositoryImpl
}
