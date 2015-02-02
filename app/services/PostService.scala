package services

/**
 * Created by svirdi on 1/1/15.
 */

import models.{Post}

trait PostService {
  def byId(id: Int, published: Boolean = true): Option[Post]
  def all: List[(Int, String)]
  def allActive: List[Post]
  def update(p: Post)
  def insert(p: Post)
}
