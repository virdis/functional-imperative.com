package models

/**
 * Created by svirdi on 1/25/15.
 */
import scalaz._
import Scalaz._

object PostValidations {

  def checkTitle(p: Post): ValidationNel[String, Post] = {
    if (p.title.isEmpty) NonEmptyList("Title cannot be empty").failure else p.success
  }

  def checkContent(p: Post): ValidationNel[String, Post] = {
    if (p.content.isEmpty) NonEmptyList("Content cannot be empty").failure else p.success
  }

  def validateP(p: Post) = {
    val VA = Applicative[({type f[a] = ValidationNel[String,a]})#f]
    VA.apply2(checkTitle(p), checkContent(p))((_,p) => p)
  }
}
