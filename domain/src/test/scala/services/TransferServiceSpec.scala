package org.nisshiee.chatwork_slack_relay.domain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }

import com.github.nscala_time.time.Imports._
import org.nisshiee.chatwork_slack_relay.domain.chatwork._
import org.nisshiee.chatwork_slack_relay.domain.slack._
import org.nisshiee.chatwork_slack_relay.test._
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.time._

class TransferServiceSpec
extends WordSpec
with OneInstancePerTest
with MockFactory
with Dummies {
  val service = new TransferService {}

  "#ctos" should {
    "converts from chatwork message to slack post" in {
      assert(service.ctos(room, message) === post)
    }
  }
}
