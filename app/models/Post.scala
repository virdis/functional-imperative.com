package models

import org.joda.time.DateTime
import scala.slick.ast.ColumnOption.DBType
import scala.slick.driver.MySQLDriver.api._

case class Post(id: Option[Int], title: String, content: String,
                isPublished: Option[Boolean], createdAt: Option[DateTime], updatedAt: Option[DateTime])

class Posts(tag: Tag) extends Table[Post](tag, "posts") {
  def id = column[Int]("postId", O.PrimaryKey, O.AutoInc, O.NotNull)
  def title = column[String]("title",  DBType("VARCHAR(255)"))
  def content = column[String]("content", DBType("LONGTEXT"))
  def isPublished = column[Boolean]("isPublished")
  def createdAt = column[DateTime]("createdAt")
  def updatedAt = column[DateTime]("updatedAt")

  def * = (id.?, title, content, isPublished.?, createdAt.?, updatedAt.?) <> (Post.tupled, Post.unapply)
}

object PostLenses {
  import scalaz._
  import Lens._
  /*
    Look into 'Monocle' library. for now use Scalaz
   */
  val setPublished = (p:Post, flag: Option[Boolean]) => p.copy(isPublished = flag)
  val getPublished = (p: Post) => p.isPublished

  val publishLens: Lens[Post, Option[Boolean]] = Lens.lensu(setPublished, getPublished)

  val createdAtLens: Lens[Post, Option[DateTime]] = Lens.lensu((p: Post, d: Option[DateTime]) => p.copy(createdAt = d), _.createdAt)

  val updatedAtLens: Lens[Post, Option[DateTime]]  = Lens.lensu((p: Post, d: Option[DateTime]) => p.copy(updatedAt = d), _.updatedAt)

  val titleLens: Lens[Post, String] = Lens.lensu((p: Post, s: String) => p.copy(title = s), _.title)

  val contentLens: Lens[Post, String] = Lens.lensu((p: Post, s: String) => p.copy(content = s), _.content)

}