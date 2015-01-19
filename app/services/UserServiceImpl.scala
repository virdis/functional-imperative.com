package services

import database.db._
import models.User
import models.SHelper._
import play.Logger
import util.Utils
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scalaz._
import Scalaz._
import scala.slick.driver.MySQLDriver.api._


/**
 * Created by svirdi on 1/11/15.
 */
object UserServiceImpl extends UserService {


  def validateUserName(uName : String): Option[String] =
    if (Utils.userName.exists(_ == uName)) uName.some else none[String]

  private [services] override def byUserNameAndPass(uname: String, pass: String): Option[User] = {
    val query = users.filter(u => u.username === uname && u.password === pass)
    Await.result(db.run(query.result), Duration.Inf).headOption
  }

  override def user(uname: String, pass: String): Option[User] = {
    validateUserName(uname) flatMap (u => byUserNameAndPass(u, pass))
  }

  def memoUser(uname: String, pass: String): Tuple2[String,String] => Option[Option[User]] = {
    import Memo._
    val pair = Tuple2(uname, pass)
    val m = Map(pair -> user(uname, pass))
    weakHashMapMemo(m.lift)
  }
}
