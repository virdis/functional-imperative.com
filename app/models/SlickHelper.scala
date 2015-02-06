package models
import database.db._

import scala.slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object SHelper {
  val posts = TableQuery[Posts]
  val tags = TableQuery[Tagz]
  val tagPost = TableQuery[TagsPosts]
  val users = TableQuery[Users]

  val schema = posts.schema ++ tags.schema ++ tagPost.schema ++ users.schema

  def createTables = Await.result(db.run(Action.seq(
    schema.create
  )), Duration.Inf)

  def dropTables = Await.result(db.run(Action.seq(
    schema.drop
  )), Duration.Inf)

  

}