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

class RelayServiceSpec
extends WordSpec
with concurrent.ScalaFutures
with OneInstancePerTest
with MockFactory
with Dummies {
  override implicit def patienceConfig =
    super.patienceConfig.copy(timeout = Span(1, Second))

  val service = new RelayService {
    val streamService  = stub[StreamService]
    val transferService = new TransferService {}
    val postRepository = stub[PostRepository]
    val roomRepository = stub[RoomRepository]
  }
  (service.postRepository.send(_: Post)(_: ExecutionContext)).
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

  def doNothingSituation(roomOpt: Option[Room], messages: List[Message]): Unit = {
    "does nothing" in {
      stubRoomRepository()(roomOpt)
      stubStreamService()(messages)

      assert(service.run(roomId).futureValue === unit)

      (service.postRepository.send(_: Post)(_: ExecutionContext)).
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

    "streamService returns a message" should {
      "calls PostRepository#send" in {
        stubRoomRepository()(Some(room))
        stubStreamService()(message :: Nil)

        assert(service.run(roomId).futureValue === unit)

        (service.postRepository.send(_: Post)(_: ExecutionContext)).
          verify(post, *).
          once
      }
    }

    "called with plural roomIds" should {
      "calls related services, repositories for each room" in {
        val otherRoomId = Id.value.set(2)(roomId)
        val otherRoom = (
          Room.id.set(otherRoomId) andThen
          Room.name.set("other room"))(room)
        stubRoomRepository()(Some(room))
        stubRoomRepository(otherRoomId)(Some(otherRoom))

        val otherMessage = (
          (Message.id ^|-> Id.value).set(2) andThen
          Message.body.set("other message"))(message)
        stubStreamService()(message :: Nil)
        stubStreamService(otherRoom)(otherMessage :: Nil)

        assert(service.run(roomId :: otherRoomId :: Nil).futureValue === unit)

        val otherExpectedPost = (
          Post.username.set("other room") andThen
          (Post.author ^<-? some ^|-> Author.link).set("https://www.chatwork.com/#!rid2-2") andThen
          Post.body.set("other message"))(post)
        (service.postRepository.send(_: Post)(_: ExecutionContext)).
          verify(post, *).
          once
        (service.postRepository.send(_: Post)(_: ExecutionContext)).
          verify(otherExpectedPost, *).
          once
      }
    }
  }
}
