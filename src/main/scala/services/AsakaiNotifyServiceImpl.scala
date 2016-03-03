package org.nisshiee.chatwork_slack_relay

import org.nisshiee.chatwork_slack_relay.chatwork._
import org.nisshiee.chatwork_slack_relay.domain._
import org.nisshiee.chatwork_slack_relay.infra.chatwork._
import org.nisshiee.chatwork_slack_relay.infra.slack._

object AsakaiNotifyServiceImpl extends AsakaiNotifyService
with MixinStreamService
with MixinPostRepository
with MixinRoomRepository

trait MixinAsakaiNotifyService extends UsesAsakaiNotifyService {
  override val asakaiNotifyService = AsakaiNotifyServiceImpl
}
