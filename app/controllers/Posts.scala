package controllers

import di.MetaConfig
import play.api._
import play.api.mvc._
import scalaz._
import Scalaz._
import Kleisli._
import services.{PostService, PostServiceImpl}
import services.ServicesHelper.{CollectionResult, SingleResult}
import models.Post

object Posts extends Controller with MetaConfig {

  def byId(id: Int) = Action { request =>
    val kf = kleisli[SingleResult, PostService, Post]( _.byId(id) |> SingleResult.createFromOption(s"Cannot find post for given id : ${id}"))
    val post = for {
      res <- kf.run
    } yield res.fold(e => Ok(views.html.index(e)), post => Ok(views.html.posts.post(post)))
    post(postService).getOrElse(NotFound)
  }

  def allActive = Action { request =>
    val kf = kleisli[CollectionResult, PostService, Post](_.all |> CollectionResult.createFromList("Collection is Empty"))
    val posts = for {
      res <- kf.run
    } yield res.fold(e => Ok(views.html.index(e)), posts => Ok(views.html.posts.posts(posts)))
    posts(postService)
  }


}