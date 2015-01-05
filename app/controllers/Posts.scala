package controllers

import di.MetaConfig
import play.api._
import play.api.mvc._
import scalaz._
import Scalaz._
import services.{PostService, PostServiceImpl}
import services.ServicesHelper.{CollectionResult, SingleResult}
import models.Post

object Posts extends Controller with MetaConfig {

  def byId(id: Int) = Action { request =>
    val r = Kleisli[SingleResult, PostService, Post] { uService =>
      val res = for {
        p <- uService.byId(id) |> SingleResult.createFromOption(s"Cannot find post for given id : ${id}")
      } yield p
      res
    }
    r.run(userService).run.map(_.fold(error => Ok(views.html.index(error)),
      (result:Post) => Ok(views.html.posts.post(result)))).getOrElse(NotFound)
  }

  def allActive = Action { request =>
    val r = Kleisli[CollectionResult, PostService, Post]{
      _.all |> CollectionResult.createFromList("Collection is Empty")
    }
    r.run(userService).fold(e => Ok(views.html.index(e)), posts => Ok(views.html.posts.posts(posts)))
  }
}