package models

import scala.slick.driver.MySQLDriver.api._
import models.SHelper._

case class TagPost(postId: Int, taglId: Int)

class TagsPosts(tag: Tag) extends Table[TagPost](tag, "tag_post") {
  def postId = column[Int]("postId")
  def tagId = column[Int]("tagId")

  def * = (postId, tagId) <> (TagPost.tupled, TagPost.unapply)

  def post = foreignKey("post_fk", postId, posts)(_.id, onUpdate=ForeignKeyAction.Restrict,
                                                  onDelete=ForeignKeyAction.Cascade)

  def label = foreignKey("tag_fk", tagId, tags)(_.id, onUpdate=ForeignKeyAction.Restrict,
                                                      onDelete=ForeignKeyAction.Cascade)

  def idx = index("tag_post_idx", (postId, tagId), unique = true)

}