package services

/**
 * Created by svirdi on 1/1/15.
 */

import models.{Post}

trait UserService {
  def byId(id: Int): Option[Post]
  def all: List[Post]
}
