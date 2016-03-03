package org.nisshiee.chatwork_slack_relay.test.slack

import org.nisshiee.chatwork_slack_relay.domain.slack._

trait Posts
extends Authors {
  val post = Post(
    username   = "room name",
    iconUrl    = "icon path",
    author     = Some(author),
    body       = "body")

  def asakaiNotifyPost(userName: String): Post =
    (Post.body.set(s"＜1＞ $userName") andThen
      Post.author.set(None))(post)
}
