package controllers

import models.gitdiscover.{RepoStats, TopRepo, TopRepos}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import models.gitdiscover.RepoStatsFormat._
/**
  * Created by sandeep on 1/30/16.
  */
object GitDiscover extends Controller {


  def topProjects = Action { request =>

    Ok(views.html.topproject(TopRepos.get))

  }

  def projectDetails = Action {
    request =>
      //Logger.info("==== "+Json.toJson(RepoStats.get))
      Ok(views.html.gitDiscover())
  }
}
