package models

import slick.driver.MySQLDriver.api._

case class Tags(id: Option[Int], name: String)

class Tagz(tag: Tag) extends Table[Tags](tag, "tags") {
  def id = column[Int]("tagId", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.SqlType("VARCHAR(150)"))

  def * = (id.?, name) <> (Tags.tupled, Tags.unapply)
}

