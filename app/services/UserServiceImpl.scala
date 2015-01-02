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

object UserServiceImpl  {

  def byId(id: Int) = {
    posts.filter(p => p.isPublished && p.id === id).result
  }

/* 
def test = {
    println("Post : "+Await.result(db.db.run(byId(1)), Duration.Inf).headOption)
  }
  * 
  */
}
