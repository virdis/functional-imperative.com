package controllers

import di.MetaConfig
import org.joda.time.DateTime
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import scalaz._
import Scalaz._
import Kleisli._
import services.{PostService, PostServiceImpl}
import services.ServicesHelper.{CollectionResult, SingleResult}
import models.Post
import play.filters.csrf._


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


  val pForm = Form(
    mapping(
      "id" ->   optional(number),
      "title" -> nonEmptyText,
      "content" -> nonEmptyText,
      "isPublished" -> optional(boolean),
      "createdAt" -> optional(jodaDate),
      "updatedAt" -> optional(jodaDate)
    )(Post.apply)(Post.unapply)
  )

  def edit(id: Int) = Action { request =>
    request.session.get("admin").map(s => {
      if (s == "success")
        PostServiceImpl.byId(id).
          map{ p => Ok(views.html.posts.create(pForm.fill(p))(request)) }
          .getOrElse(NotFound)
      else
        Forbidden
    }).getOrElse(Forbidden)

  }

  def postForm() = Action { request =>
    request.session.get("admin").map(s => {
      if (s == "success")
        Ok(views.html.posts.create(pForm)(request))
      else
        Redirect("posts/all")
    }).getOrElse(Redirect("posts/all"))

  }

  def create() =
    Action { implicit  request =>
      request.session.get("admin").map(s => {
        if (s == "success")
          pForm.bindFromRequest.fold(
            e => BadRequest(views.html.posts.create(e)),
            vData => {
              Logger.info("VData "+vData)
              PostServiceImpl.insert(vData)
              Ok
            }
          )
        else
          Forbidden
      }).getOrElse(Forbidden)
    }


  def mainBlog = Action{ implicit request => Ok(views.html.blogindex()) }


}