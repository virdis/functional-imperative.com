package models

/**
 * Created by svirdi on 1/4/15.
 */

import slick.driver.MySQLDriver.api._

case class User(id: Option[Int], username: String, password: String)

class Users(tag: Tag) extends Table[User](tag, "users"){
  def id = column[Int]("userId", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username", O.SqlType("VARCHAR(150)"))
  def password = column[String]("password", O.SqlType("VARCHAR(255)"))

  def * = (id.?, username, password) <> (User.tupled, User.unapply)
}
