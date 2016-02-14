package org.nisshiee.chatwork_slack_relay.infra.chatwork

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document._
import com.github.nscala_time.time.Imports._
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import org.nisshiee.chatwork_slack_relay.domain.chatwork._

object LastLoadedRepositoryImpl extends LastLoadedRepository {
  lazy val config = ConfigFactory.load
  lazy val region = Regions.fromName(config.as[String]("aws.region"))
  lazy val tableName = config.as[String]("aws.dynamodb.lastLoaded.table")
  lazy val table = {
    val client: AmazonDynamoDBClient = new AmazonDynamoDBClient(
      new EnvironmentVariableCredentialsProvider()
    ).withRegion(region)
    val dynamoDB = new DynamoDB(client)
    dynamoDB.getTable(tableName)
  }

  lazy val roomIdKey = "roomId"
  lazy val sendTimeKey = "sendTime"
  lazy val updateTimeKey = "updateTime"

  implicit class RichItem(val self: Item) extends AnyVal {
    def getDateTime(name: String): Option[DateTime] =
      Try(self.getLong(name)).toOption.map { l: Long => new DateTime(l) }
  }

  override def get(room: Room)(implicit ec: ExecutionContext) = Future {
    (for {
      item <- Option(table.getItem(new KeyAttribute(roomIdKey, room.id.value)))
      sendTime <- item.getDateTime(sendTimeKey)
      updateTime <- item.getDateTime(updateTimeKey)
    } yield LastLoaded(sendTime, updateTime)).getOrElse(LastLoaded.default)
  }

  override def set(room: Room, lastLoaded: LastLoaded)(implicit ec: ExecutionContext) = Future {
    val item = new Item().
      withPrimaryKey(roomIdKey, room.id.value).
      withLong(sendTimeKey, lastLoaded.sendTime.getMillis).
      withLong(updateTimeKey, lastLoaded.updateTime.getMillis)
    table.putItem(item)
    ()
  }
}

trait MixinLastLoadedRepository extends UsesLastLoadedRepository {
  override val lastLoadedRepository = LastLoadedRepositoryImpl
}
