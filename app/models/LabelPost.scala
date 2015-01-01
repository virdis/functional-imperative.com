package models

import scala.slick.driver.MySQLDriver.simple._
import models.SHelper._

case class LabelPost(postId: Int, labelId: Int)

class LabelPosts(tag: Tag) extends Table[LabelPost](tag, "label_post") {
  def postId = column[Int]("postId")
  def labelId = column[Int]("labelId")

  def * = (postId, labelId) <> (LabelPost.tupled, LabelPost.unapply)

  def post = foreignKey("post_fk", postId, posts)(_.id, onUpdate=ForeignKeyAction.Restrict,
                                                  onDelete=ForeignKeyAction.Cascade)

  def label = foreignKey("label_fk", labelId, labels)(_.id, onUpdate=ForeignKeyAction.Restrict,
                                                      onDelete=ForeignKeyAction.Cascade)

  def idx = index("label_post_idx", (postId, labelId), unique = true)

}