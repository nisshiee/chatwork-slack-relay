package org.nisshiee.chatwork_lambda_test.domain

import com.github.nscala_time.time.Imports._

trait CurrentTimeRepository {
  def get: DateTime
}

trait UsesCurrentTimeRepository {
  val currentTimeRepository: CurrentTimeRepository
}
