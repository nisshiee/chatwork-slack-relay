package org.nisshiee.chatwork_slack_relay

import org.nisshiee.chatwork_slack_relay.domain._

object TransferServiceImpl extends TransferService

trait MixinTransferService extends UsesTransferService {
  override val transferService = TransferServiceImpl
}
