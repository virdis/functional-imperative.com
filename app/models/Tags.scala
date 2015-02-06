package models

import scala.slick.ast.ColumnOption.DBType
import scala.slick.driver.MySQLDriver.api._

case class Tags(id: Option[Int], name: String)

class Tagz(tag: Tag) extends Table[Tags](tag, "tags") {
  def id = column[Int]("tagId", O.PrimaryKey, O.AutoInc, O.NotNull)
  def name = column[String]("name", DBType("VARCHAR(150)"))

  def * = (id.?, name) <> (Tags.tupled, Tags.unapply)
}

