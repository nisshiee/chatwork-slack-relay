package org.nisshiee.chatwork_slack_relay.domain

import org.nisshiee.chatwork_slack_relay.domain.chatwork.{ Message, Room }
import org.nisshiee.chatwork_slack_relay.domain.slack.Post

trait TransferService {
  def ctos(room: Room, message: Message): Post = Post(
    room.name,
    room.iconPath,
    message.account.name,
    s"https://www.chatwork.com/#!rid${room.id}-${message.id}",
    message.account.avatarImageUrl,
    message.body)
}

trait UsesTransferService {
  val transferService: TransferService
}
