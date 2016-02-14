package org.nisshiee.chatwork_slack_relay

import org.nisshiee.chatwork_slack_relay.chatwork._
import org.nisshiee.chatwork_slack_relay.domain._
import org.nisshiee.chatwork_slack_relay.infra.chatwork._
import org.nisshiee.chatwork_slack_relay.infra.slack._

object RelayServiceImpl extends RelayService
with MixinStreamService
with MixinPostRepository
with MixinRoomRepository

trait MixinRelayService extends UsesRelayService {
  override val relayService = RelayServiceImpl
}
