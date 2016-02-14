package org.nisshiee.chatwork_slack_relay.chatwork

import org.nisshiee.chatwork_slack_relay.domain._
import org.nisshiee.chatwork_slack_relay.domain.chatwork._
import org.nisshiee.chatwork_slack_relay.infra._
import org.nisshiee.chatwork_slack_relay.infra.chatwork._

object StreamServiceImpl extends StreamService
with MixinMessageRepository
with MixinLastLoadedRepository
with MixinCurrentTimeRepository

trait MixinStreamService extends UsesStreamService {
  override val streamService = StreamServiceImpl
}
