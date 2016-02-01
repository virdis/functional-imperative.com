package controllers

import models.gitdiscover.{RepoStats, TopRepo, TopRepos}
import play.api.Logger
import play.api.mvc.{Action, Controller}

/**
  * Created by sandeep on 1/30/16.
  */
object GitDiscover extends Controller {


  def topProjects = Action { request =>

    Ok(views.html.topproject(TopRepos.get))

  }

  def projectDetails = Action {
    request =>
      Logger.info("==== "+RepoStats.get)
      Ok(views.html.gitDiscover())
  }
}
