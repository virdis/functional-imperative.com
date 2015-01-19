package models
import database.db._

import scala.slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object SHelper {
  val posts = TableQuery[Posts]
  val labels = TableQuery[Lables]
  val labelPost = TableQuery[LabelPosts]
  val users = TableQuery[Users]

  val schema = posts.schema ++ labels.schema ++ labelPost.schema ++ users.schema

  def createTables = Await.result(db.run(Action.seq(
    schema.create
  )), Duration.Inf)
    //db.withSession{ implicit  s => schema.create }

  def dropTables = Await.result(db.run(Action.seq(
    schema.drop
  )), Duration.Inf)
    //db.withSession{ implicit s => schema.drop }
  

}