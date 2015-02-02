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
    } yield res.fold(e => Ok(views.html.index(e)), post => Ok(views.html.posts.post(post.point[List])))
    post(postService).getOrElse(NotFound)
  }

  def allActive = Action { request =>
    val kf = kleisli[CollectionResult, PostService, Post](_.allActive |> CollectionResult.createFromList("Collection is Empty"))
    val posts = for {
      res <- kf.run
    } yield res.fold(e => Ok(views.html.index(e)), posts => Ok(views.html.allActive(posts)))
    posts(postService)
  }

  def all = Action { implicit request =>
    if (checkAdmin) {
      val kf = kleisli[CollectionResult, PostService, (Int, String)](_.all |> CollectionResult.createFromList("Collection is Empty"))
      val allposts = for {
        res <- kf.run
      } yield res.fold(e => Ok(views.html.index(e)), ids => Ok(views.html.posts.dashboard(ids)))
      allposts(postService)
    }
    else
      Forbidden
  }


  val pForm = Form(
    mapping(
      "id" ->   optional(number),
      "title" -> nonEmptyText,
      "description" -> nonEmptyText,
      "content" -> nonEmptyText,
      "isPublished" -> optional(boolean),
      "createdAt" -> optional(jodaDate),
      "updatedAt" -> optional(jodaDate)
    )(Post.apply)(Post.unapply)
  )

  def editPost(id: Int) = Action { implicit request =>
    if (checkAdmin) {
      val kf = kleisli[SingleResult, PostService, Post](_.byId(id, false) |> SingleResult.createFromOption("Post not found"))
      val post = for {p <- kf.run} yield p.fold(e => Forbidden, p => Ok(views.html.posts.editPost(pForm.fill(p))(request)))
      post(postService).getOrElse(Forbidden)
    }
    else
      Forbidden


  }

  def updatePost = Action { implicit request =>
    if (checkAdmin)
      pForm.bindFromRequest.fold(
      e => BadRequest(views.html.posts.editPost(e)),
      p => {
        val up: Reader[PostService, Unit] = Reader(_.update(p))
        up.run(postService)
        Ok
      })
    else
      Forbidden
  }

  def postForm() = Action { implicit request =>
    if (checkAdmin)
      Ok(views.html.posts.create(pForm)(request))
    else
      Redirect("posts/all")


  }

  def create() =
    Action { implicit  request =>
      if (checkAdmin)
        pForm.bindFromRequest.fold(
          e => BadRequest(views.html.posts.create(e)),
          p => {
            val ip: Reader[PostService, Unit] = Reader(_.insert(p))
            ip.run(postService)
            Ok
          })
      else
        Forbidden

    }


  def about = Action { request =>
    Ok(views.html.about(List()))
  }


  def checkAdmin(implicit request: Request[_]): Boolean = {
    if(request.session.get("admin").getOrElse("") == "success") true else false
  }

}
