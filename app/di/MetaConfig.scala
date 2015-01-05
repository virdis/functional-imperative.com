package di

import services.{PostServiceImpl, PostService}

/**
 * Created by svirdi on 1/4/15.
 */
trait MetaConfig {

  // declare Dependencies can be made more flexible
  val userService: PostService = PostServiceImpl
}
