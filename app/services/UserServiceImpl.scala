package services

/**
 * Created by svirdi on 1/1/15.
 */

import database.db._

import models.{Posts, Post}
import models.SHelper._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.slick.driver.MySQLDriver.api._
import scalaz._
import scalaz.syntax.either._

object UserServiceImpl extends UserService  {

  def byId(id: Int): Option[Post] = {
    val query = posts.filter(p => p.isPublished && p.id === id)
    Await.result(db.run(query.result), Duration.Inf).headOption
  }

  def all: List[Post] = {
    val query = posts.filter(p => p.isPublished)
    Await.result(db.run(query.result), Duration.Inf).toList
  }


}
