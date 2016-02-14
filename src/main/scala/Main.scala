package org.nisshiee.chatwork_lambda_test

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import com.amazonaws.services.lambda.runtime.Context
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import org.nisshiee.chatwork_lambda_test.domain._
import org.nisshiee.chatwork_lambda_test.domain.chatwork._

class Main extends MixinRelayService {
  lazy val config = ConfigFactory.load
  lazy val targetRoomId = Id[Room](config.as[Long]("chatwork.targetRoomId"))

  def main(input: String, context: Context): String = {
    val future = relayService.run(targetRoomId).
      map { _ => "done" }.
      recover {
        case t => t.toString
      }
    Await.result(future, Duration.Inf)
  }
}
