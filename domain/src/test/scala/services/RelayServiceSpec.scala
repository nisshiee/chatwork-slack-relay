package org.nisshiee.chatwork_lambda_test.domain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }

import com.github.nscala_time.time.Imports._
import org.nisshiee.chatwork_lambda_test.domain.chatwork._
import org.nisshiee.chatwork_lambda_test.domain.slack._
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class RelayServiceSpec
extends WordSpec
with concurrent.ScalaFutures
with OneInstancePerTest
with MockFactory {
  val roomId     = Id[Room](1)
  val room       = Room(roomId, "room name", "icon path")
  val account    = Account(
    Id[Account](1L),
    "name",
    "http://avater.img/url")
  val message    = Message(
    Id[Message](1L),
    account,
    "body",
    new DateTime(2016, 2, 10, 12, 33, 44),
    new DateTime(0L))
  val expectedPost = Post(
    username   = "room name",
    iconUrl    = "icon path",
    author     = "name",
    authorIcon = "http://avater.img/url",
    body       = "body")
  val unit = ()

  val service = new RelayService {
    val streamService  = stub[StreamService]
    val postRepository = stub[PostRepository]
    val roomRepository = stub[RoomRepository]
  }

  "#toSlackPost" should {
    "converts from chatwork message to slack post" in {
      assert(service.toSlackPost(room, message) === expectedPost)
    }
  }

  def doNothingSituation(roomOpt: Option[Room], messages: List[Message]): Unit = {
    "does nothing" in {
      (service.roomRepository.get(_: Id[Room])(_: ExecutionContext)).
        when(roomId, *).
        returns(Future.successful(roomOpt))
      (service.streamService.messageStream(_: Room)(_: ExecutionContext)).
        when(room, *).
        returns(Future.successful(messages))

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
        (service.roomRepository.get(_: Id[Room])(_: ExecutionContext)).
          when(roomId, *).
          returns(Future.successful(Some(room)))
        (service.streamService.messageStream(_: Room)(_: ExecutionContext)).
          when(room, *).
          returns(Future.successful(message :: Nil))
        (service.postRepository.send(_: Post)(_: ExecutionContext)).
          when(*, *).
          returns(Future.successful(unit))

        assert(service.run(roomId).futureValue === unit)

        (service.postRepository.send(_: Post)(_: ExecutionContext)).
          verify(expectedPost, *)
      }
    }
  }
}
