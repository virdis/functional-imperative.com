package controllers

import models.gitdiscover.{TopRepo, TopRepos}
import play.api.mvc.{Action, Controller}

/**
  * Created by sandeep on 1/30/16.
  */
object GitDiscover extends Controller {


  def topProjects = Action { request =>

    Ok(views.html.topproject(TopRepos.get))

  }
}
