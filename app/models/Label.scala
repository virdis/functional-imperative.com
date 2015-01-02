package models

import scala.slick.ast.ColumnOption.DBType
import scala.slick.driver.MySQLDriver.api._

case class Label(id: Option[Int], name: String)

class Lables(tag: Tag) extends Table[Label](tag, "labels") {
  def id = column[Int]("labelId", O.PrimaryKey, O.AutoInc, O.NotNull)
  def name = column[String]("name", DBType("VARCHAR(150)"))

  def * = (id.?, name) <> (Label.tupled, Label.unapply)
}

