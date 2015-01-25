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
import models.dateTime

object PostServiceImpl extends PostService  {

  def byId(id: Int, published: Boolean = true): Option[Post] = {
    val query = if (published)
      posts.filter(p => p.id === id && p.isPublished === published)
    else
      posts.filter(p => p.id === id)
    Await.result(db.run(query.result), Duration.Inf).headOption
  }

  def all: List[(Int, String)] = {
    val query = posts.map(p => (p.id, p.title))
    Await.result(db.run(query.result), Duration.Inf).toList
  }

  def allActive: List[Post] = {
    val query = posts.filter(p => p.isPublished)
    Await.result(db.run(query.result), Duration.Inf).toList
  }

  def insert(post: Post) = {
    val p = updatedAtLens.set(createdAtLens.set(post, Option(new DateTime())) , Option(new DateTime()))
    Await.result(db.run((posts += publishLens.set(p, Some(false)))), Duration.Inf)
  }

  def update(p: Post) = {
    val query = posts.filter(_.id === p.id)
      .map(d => (d.title, d.content, d.updatedAt))
    Await.result(db.run(query.update(p.title, p.content, new DateTime())), Duration.Inf)

  }

}
