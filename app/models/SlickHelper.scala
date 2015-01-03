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

  val schema = posts.schema ++ labels.schema ++ labelPost.schema

  def createTables = db.withSession{ implicit  s => schema.create }
  

}