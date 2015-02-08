package services

/**
 * Created by svirdi on 1/1/15.
 */

import models.{Post}

trait PostService {
  import PostService._

  def byId(id: Int, published: Boolean = true): Option[Post]
  def all: List[(Int, String)]
  def allActive(pageNumber: Int = ARTICLES_PER_PAGE): List[Post]
  def update(p: Post)
  def insert(p: Post)
}

object PostService {  
  val ARTICLES_PER_PAGE = 3
}
