package org.nisshiee.chatwork_slack_relay.infra.slack

import scala.concurrent.{ ExecutionContext, Future }

import dispatch._, Defaults._
import org.json4s._
import org.json4s.jackson.Serialization
import org.nisshiee.chatwork_slack_relay.domain.slack._
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._

object PostRepositoryImpl extends PostRepository {
  val config = ConfigFactory.load
  val hookUrl = config.as[String]("slack.hookUrl")

  object json {
    case class Attachment(
      author_name: Option[String] = None,
      author_icon: Option[String] = None,
      text: Option[String] = None)
    case class Post(
      username: Option[String] = None,
      icon_url: Option[String] = None,
      attachments: List[Attachment] = Nil)
  }

  override def send(post: Post)(implicit ec: ExecutionContext) = {
    val req = url(hookUrl).
      <<(post2json(post)).
      setContentType("application/json", "UTF-8")

    Http(req OK as.String).
      map(println)
  }

  implicit lazy val formats = DefaultFormats

  private def post2json(post: Post): String = {
    val jsonObj = json.Post(
      Some(post.username),
      Some(post.iconUrl),
      List(json.Attachment(
        Some(post.author),
        Some(post.authorIcon),
        Some(post.body))))

    Serialization.write(jsonObj)
  }
}

trait MixinPostRepository extends UsesPostRepository {
  override val postRepository = PostRepositoryImpl
}