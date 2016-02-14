package org.nisshiee.chatwork_slack_relay.domain.chatwork

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }

import com.github.nscala_time.time.Imports._
import org.nisshiee.chatwork_slack_relay.domain._
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class StreamServiceSpec
extends WordSpec
with concurrent.ScalaFutures
with OneInstancePerTest
with MockFactory {
  val room       = Room(Id[Room](1), "room name", "icon path")
  val now        = new DateTime(2016, 2, 10, 12, 34, 56)
  val yesterday  = new DateTime(2016, 2,  9, 12, 34, 56)
  val lastLoaded = LastLoaded(
    new DateTime(2016, 2,  9, 23, 12, 23),
    new DateTime(2016, 2, 10,  1, 23, 45))
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

  val service = new StreamService {
    override val messageRepository     = stub[MessageRepository]
    override val currentTimeRepository = stub[CurrentTimeRepository]
    override val lastLoadedRepository  = stub[LastLoadedRepository]
  }

  def setupStub(repoMessages: List[Message] = Nil): Unit = {
    (service.currentTimeRepository.get _).when.returns(now)
    (service.lastLoadedRepository.get(_: Room)(_: ExecutionContext)).
      when(room, *).
      returns(Future.successful(lastLoaded))
    (service.lastLoadedRepository.set(_: Room, _: LastLoaded)(_: ExecutionContext)).
      when(room, *, *).
      returns(Future.successful(Unit))
    (service.messageRepository.latest(_: Room)(_: ExecutionContext)).
      when(room, *).
      returns(Future.successful(repoMessages))
  }

  def setupStub(message: Message): Unit = setupStub(message :: Nil)

  def emptyResult(setupStub: => Unit): Unit = {
    "returns empty" in {
      setupStub
      assert(service.messageStream(room).futureValue === Nil)
    }

    "saves same lastLoaded" in {
      setupStub
      service.messageStream(room).futureValue

      (service.lastLoadedRepository.set(_: Room, _: LastLoaded)(_: ExecutionContext)).
        verify(room, lastLoaded, *)
    }
  }

  "#messageStream" when {
    "messageRepository returns empty" should {
      behave like emptyResult(setupStub())
    }

    "messageRepository returns message before yesterday" should {
      val m = (Message.sendTime.set(yesterday - 1.hour) andThen
        Message.updateTime.set(yesterday - 2.hour))(message)
      behave like emptyResult(setupStub(m))
    }

    "messageRepository returns message after yesterday, after last sendTime, before last udpateTime" should {
      "returns that message" in {
        setupStub(message)
        assert(service.messageStream(room).futureValue === message :: Nil)
      }

      "saves new loadedTime updated only sendTime" in {
        setupStub(message)
        service.messageStream(room).futureValue

        (service.lastLoadedRepository.set(_: Room, _: LastLoaded)(_: ExecutionContext)).
          verify(room, LastLoaded.sendTime.set(message.sendTime)(lastLoaded), *)
      }
    }

    "messageRepository returns message after yesterday, before last sendTime, after last udpateTime" should {
      val m = (Message.sendTime.set(lastLoaded.sendTime - 1.hour) andThen
        Message.updateTime.set(lastLoaded.updateTime + 1.hour))(message)

      "returns that message" in {
        setupStub(m)
        assert(service.messageStream(room).futureValue === m :: Nil)
      }

      "saves new loadedTime updated only updateTime" in {
        setupStub(m)
        service.messageStream(room).futureValue

        (service.lastLoadedRepository.set(_: Room, _: LastLoaded)(_: ExecutionContext)).
          verify(room, LastLoaded.updateTime.set(m.updateTime)(lastLoaded), *)
      }
    }

    "messageRepository returns message after yesterday, after last sendTime, after last udpateTime" should {
      val m = (Message.sendTime.set(lastLoaded.sendTime + 1.hour) andThen
        Message.updateTime.set(lastLoaded.updateTime + 1.hour))(message)

      "returns that message" in {
        setupStub(m)
        assert(service.messageStream(room).futureValue === m :: Nil)
      }

      "saves new loadedTime updated both sendTime and updateTime" in {
        setupStub(m)
        service.messageStream(room).futureValue

        val expectedNewLastLoaded = (LastLoaded.sendTime.set(m.sendTime) andThen
          LastLoaded.updateTime.set(m.updateTime))(lastLoaded)
        (service.lastLoadedRepository.set(_: Room, _: LastLoaded)(_: ExecutionContext)).
          verify(room, expectedNewLastLoaded, *)
      }
    }
  }
}
