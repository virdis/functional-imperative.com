package services

import models.User
import ServicesHelper._

import scalaz.{\/, Validation}


/**
 * Created by svirdi on 1/4/15.
 */
trait UserService {

  private [services] def byUserNameAndPass(uname: String,pass: String): Option[User]

  def user(uname: String, pass: String):  Option[User]
}
