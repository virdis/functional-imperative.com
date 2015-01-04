package di

import services.{UserServiceImpl, UserService}

/**
 * Created by svirdi on 1/4/15.
 */
trait MetaConfig {

  // declare Dependencies can be made more flexible
  val userService: UserService = UserServiceImpl
}
