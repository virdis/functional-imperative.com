package models

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}
import slick.driver.MySQLDriver.api._

case class Post(id: Option[Int], title: String, description: String, content: String,
                isPublished: Option[Boolean], createdAt: Option[DateTime], updatedAt: Option[DateTime])

class Posts(tag: Tag) extends Table[Post](tag, "posts") {
  def id = column[Int]("postId", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title",  O.SqlType("VARCHAR(150)"))
  def description = column[String]("description", O.SqlType("VARCHAR(255)"))
  def content = column[String]("content", O.SqlType("LONGTEXT"))
  def isPublished = column[Boolean]("isPublished")
  def createdAt = column[DateTime]("createdAt")
  def updatedAt = column[DateTime]("updatedAt")

  def * = (id.?, title, description, content, isPublished.?, createdAt.?, updatedAt.?) <> (Post.tupled, Post.unapply)
}

object PostHelper {
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

  val descLens: Lens[Post, String] = Lens.lensu((p: Post, s: String) => p.copy(description = s), _.description)

  def formatDate(dt: Option[DateTime]): String = {
    val fmt = DateTimeFormat.forPattern("MMMM d, Y");
    dt.map(fmt.print(_)).getOrElse("")

  }

  val random = new scala.util.Random( System.currentTimeMillis )
  val pics = List("bikes_header.jpg", "dungar.jpg", "dungar2.jpg", "fall_colors.jpg", "jiufen.jpg", "muir_woods.jpg", "queenshead.jpg", "rockycoast.jpg", "scenicpoint.jpg")
  def getRandomHeaderPic = random.shuffle( pics ).head

}
