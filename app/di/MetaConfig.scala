package di

import services.{UserServiceImpl, UserService}

/**
 * Created by svirdi on 1/4/15.
 */
trait MetaConfig {

  val userService: UserService = UserServiceImpl
}
