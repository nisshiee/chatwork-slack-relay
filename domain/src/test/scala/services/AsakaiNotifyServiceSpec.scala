package org.nisshiee.chatwork_slack_relay.domain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }

import com.github.nscala_time.time.Imports._
import monocle.std.option.some
import org.nisshiee.chatwork_slack_relay.domain.chatwork._
import org.nisshiee.chatwork_slack_relay.domain.slack._
import org.nisshiee.chatwork_slack_relay.test._
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.time._

class AsakaiNotifyServiceSpec
extends WordSpec
with concurrent.ScalaFutures
with OneInstancePerTest
with MockFactory
with Dummies {
  override implicit def patienceConfig =
    super.patienceConfig.copy(timeout = Span(1, Second))

  val service = new AsakaiNotifyService {
    val streamService  = stub[StreamService]
    val postRepository = stub[PostRepository]
    val roomRepository = stub[RoomRepository]
  }
  (service.postRepository.sendSequencially(_: List[Post])(_: ExecutionContext)).
    when(*, *).
    returns(Future.successful(unit))

  def stubRoomRepository(id: Id[Room] = roomId)(roomOpt: Option[Room] = None): Unit =
    (service.roomRepository.get(_: Id[Room])(_: ExecutionContext)).
      when(id, *).
      returns(Future.successful(roomOpt))

  def stubStreamService(room: Room = room)(messages: List[Message] = Nil): Unit =
    (service.streamService.messageStream(_: Room)(_: ExecutionContext)).
      when(room, *).
      returns(Future.successful(messages))

  def doNothingSituation(roomOpt: Option[Room], messages: List[Message], targetUserName: String = "hoge"): Unit = {
    "does nothing" in {
      stubRoomRepository()(roomOpt)
      stubStreamService()(messages)

      assert(service.run(roomId, targetUserName).futureValue === unit)

      (service.postRepository.sendSequencially(_: List[Post])(_: ExecutionContext)).
        when(*, *).
        never()
    }
  }

  "#run" when {
    "roomRepository returns None" should {
      behave like doNothingSituation(None, Nil)
    }

    "streamService returns Nil" should {
      behave like doNothingSituation(Some(room), Nil)
    }

    "streamService returns messages that is NOT for asakai team" should {
      behave like doNothingSituation(Some(room), message :: Nil)
    }

    "streamService returns asakai team message" when {
      "target user name can't be found in the message" should {
        behave like doNothingSituation(
          Some(room),
          asakaiTeamMessage("hoge") :: Nil,
          targetUserName = "fuga")
      }

      "target user name is found in the message" should {
        "calls PostRepository#sendSequencially" in {
          stubRoomRepository()(Some(room))
          stubStreamService()(asakaiTeamMessage("hoge") :: Nil)

          assert(service.run(roomId, "hoge").futureValue === unit)

          (service.postRepository.sendSequencially(_: List[Post])(_: ExecutionContext)).
            verify(asakaiNotifyPost("hoge") :: Nil, *).
            once
        }
      }
    }
  }
}
