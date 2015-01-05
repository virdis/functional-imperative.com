package models

/**
 * Created by svirdi on 1/4/15.
 */

import scala.slick.ast.ColumnOption.DBType
import scala.slick.driver.MySQLDriver.api._

case class User(id: Option[Int], username: String, password: String)

class Users(tag: Tag) extends Table[User](tag, "users"){
  def id = column[Int]("userId", O.PrimaryKey, O.NotNull, O.AutoInc)
  def username = column[String]("username", DBType("VARCHAR(150)"))
  def password = column[String]("password", DBType("VARCHAR(255)"))

  def * = (id.?, username, password) <> (User.tupled, User.unapply)
}
