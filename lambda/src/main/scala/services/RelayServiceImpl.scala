package org.nisshiee.chatwork_lambda_test

import org.nisshiee.chatwork_lambda_test.chatwork._
import org.nisshiee.chatwork_lambda_test.domain._
import org.nisshiee.chatwork_lambda_test.infra.chatwork._
import org.nisshiee.chatwork_lambda_test.infra.slack._

object RelayServiceImpl extends RelayService
with MixinStreamService
with MixinPostRepository
with MixinRoomRepository

trait MixinRelayService extends UsesRelayService {
  override val relayService = RelayServiceImpl
}
