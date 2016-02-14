package org.nisshiee.chatwork_slack_relay

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import com.amazonaws.services.lambda.runtime.Context
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import org.nisshiee.chatwork_slack_relay.domain._
import org.nisshiee.chatwork_slack_relay.domain.chatwork._

class Main extends MixinRelayService {
  lazy val config = ConfigFactory.load
  lazy val targetRoomIds: List[Id[Room]] =
    config.as[List[Long]]("chatwork.targetRoomIds").
    map(Id.apply[Room])

  def main(input: String, context: Context): String = {
    val future = relayService.run(targetRoomIds).
      map { _ => "done" }.
      recover {
        case t => t.toString
      }
    Await.result(future, Duration.Inf)
  }
}
