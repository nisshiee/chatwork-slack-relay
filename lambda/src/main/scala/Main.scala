package org.nisshiee.chatwork_lambda_test

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import com.amazonaws.services.lambda.runtime.Context
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import org.nisshiee.chatwork_lambda_test.chatwork._
import org.nisshiee.chatwork_lambda_test.domain._
import org.nisshiee.chatwork_lambda_test.domain.chatwork._

class Main extends MixinStreamService {
  lazy val config = ConfigFactory.load
  lazy val targetRoom = Room(
    Id[Room](config.as[Long]("chatwork.targetRoomId")))

  def main(input: String, context: Context): String = {
    val future = streamService.messageStream(targetRoom).
      map(_.toString).
      recover {
        case t => t.toString
      }
    Await.result(future, Duration.Inf)
  }

  def saveToDynamoDB(input: String): Unit = {
    import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
    import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
    import com.amazonaws.services.dynamodbv2.document.{ DynamoDB, Item }
    import com.amazonaws.regions.Regions

    val client: AmazonDynamoDBClient = new AmazonDynamoDBClient(
      new EnvironmentVariableCredentialsProvider()
    ).withRegion(Regions.AP_NORTHEAST_1)
    val dynamoDB = new DynamoDB(client)
    val table = dynamoDB.getTable("chatwork-lambda-test")

    val item = new Item().
      withPrimaryKey("roomId", System.currentTimeMillis).
      withString("input", input)
    table.putItem(item)
  }
}
