package controllers

import play.api._
import play.api.mvc._
import scalaz._
import Scalaz._
import services.UserServiceImpl
import services.ServicesHelper.{CollectionResult, SingleResult}
import models.Post

object Posts extends Controller {

  
  def byId(id: Int) = Action { request =>
    val res = for {
      p <- UserServiceImpl.byId(id) |> SingleResult.createFromOption(s"Cannot find post for given id : ${id}")
    } yield p
    res.run.map(_.fold(error => Ok(views.html.index(error)),
        (result:Post) => Ok(views.html.posts.post(result)))).getOrElse(NotFound)
  }

  def allActive = Action { request =>
    val res: CollectionResult[Post] = for {
      l <- UserServiceImpl.all |> CollectionResult.createFromList("Collection is empty")
    } yield l
    res.fold(e => Ok(views.html.index(e)), posts => Ok(views.html.posts.posts(posts)))

  }
}