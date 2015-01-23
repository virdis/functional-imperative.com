package services

/**
 * Created by svirdi on 1/1/15.
 */

import database.db._

import models.{PostLenses, Posts, Post}
import models.SHelper._
import org.joda.time.DateTime
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.slick.driver.MySQLDriver.api._
import scalaz._
import scalaz.syntax.either._
import PostLenses._

object PostServiceImpl extends PostService  {

  def byId(id: Int): Option[Post] = {
    val query = posts.filter(p => p.isPublished && p.id === id)
    Await.result(db.run(query.result), Duration.Inf).headOption
  }

  def all: List[Post] = {
    val query = posts.filter(p => p.isPublished)
    Await.result(db.run(query.result), Duration.Inf).toList
  }

  def insert(post: Post) = {
    val p = updatedAtLens.set(createdAtLens.set(post, Option(new DateTime())) , Option(new DateTime()))
    Await.result(db.run((posts += publishLens.set(p, Some(true)))), Duration.Inf)
  }

}
