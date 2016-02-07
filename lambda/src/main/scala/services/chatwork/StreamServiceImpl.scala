package org.nisshiee.chatwork_lambda_test.chatwork

import org.nisshiee.chatwork_lambda_test.domain._
import org.nisshiee.chatwork_lambda_test.domain.chatwork._
import org.nisshiee.chatwork_lambda_test.infra._
import org.nisshiee.chatwork_lambda_test.infra.chatwork._

object StreamServiceImpl extends StreamService
with MixinMessageRepository
with MixinCurrentTimeRepository

trait MixinStreamService extends UsesStreamService {
  override val streamService = StreamServiceImpl
}
