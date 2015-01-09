package services

import models.User

/**
 * Created by svirdi on 1/4/15.
 */
trait UserService {

  def byUserName(uname: String): Option[User]
}
