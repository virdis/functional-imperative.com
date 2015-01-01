package models

import org.joda.time.DateTime
import scala.slick.ast.ColumnOption.DBType
import scala.slick.driver.MySQLDriver.simple._

case class Post(id: Option[Int], title: String, content: String,
                isPublished: Boolean, createdAt: DateTime, updatedAt: DateTime)

class Posts(tag: Tag) extends Table[Post](tag, "posts") {
  def id = column[Int]("postId", O.PrimaryKey, O.AutoInc, O.NotNull)
  def title = column[String]("title",  DBType("VARCHAR(255)"))
  def content = column[String]("content", DBType("LONGTEXT"))
  def isPublished = column[Boolean]("isPublished")
  def createdAt = column[DateTime]("createdAt")
  def updatedAt = column[DateTime]("updatedAt")

  def * = (id.?, title, content, isPublished, createdAt, updatedAt) <> (Post.tupled, Post.unapply)
}