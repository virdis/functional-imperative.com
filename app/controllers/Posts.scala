package controllers

import play.api._
import play.api.mvc._
import scalaz._
import Scalaz._
import services.UserServiceImpl
import services.ServicesHelper.SingleResult
import models.Post

object Posts extends Controller {

  
  def byId(id: Int) = Action { request =>
    val res = for {
      p <- UserServiceImpl.byId(id) |> SingleResult.createFromOption(s"Cannot find post for given id : ${id}")
    } yield p
    res.run.map(_.fold(error => Ok(views.html.index(error)),
        (result:Post) => Ok(views.html.posts.post(result)))).getOrElse(NotFound)
  } 
}