package controllers

import models.User
import play.Logger
import play.api.mvc.{Action, Controller}
import play.api.data._
import play.api.data.Forms._
import services.UserServiceImpl._
import play.api.mvc._
import play.filters.csrf._
/**
 * Created by svirdi on 1/11/15.
 */
object Users extends Controller {

  case class LoginData(uname: String, pass: String)

  val loginForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginData.apply)(LoginData.unapply)
  )

  def login =
    Action { request =>
      Ok(views.html.users.login(loginForm)(request))
    }


  def authenticate =
      Action { implicit request =>
        loginForm.bindFromRequest.fold(e => BadRequest(views.html.users.login(e)),
          formData => {
            val user: Option[Option[User]] = memoUser(formData.uname, formData.pass)(formData.uname, formData.pass)
            user.map { optUser =>
              if (optUser.isDefined)
                Redirect("posts").withSession(request.session + ("admin" -> "success"))
              else
                Redirect("/svirdi") }.getOrElse(Redirect("/svirdi"))
          })

  }


}
