package controllers

import models.gitdiscover.{UserActivity, RepoStats, TopRepos}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import models.gitdiscover.RepoStatsFormat._
import models.gitdiscover.UserActivityFormat._
/**
  * Created by sandeep on 1/30/16.
  */
object GitDiscover extends Controller {


  def topProjects = Action { request =>

    Ok(views.html.topproject(TopRepos.get))

  }

  def projectDetails = Action {
    request =>
      Logger.info("====")
      Ok(views.html.projectdetails())
  }

  def projectTimeSeries(name: String, ymonth: String) = Action {
    request =>
      Ok(Json.toJson(RepoStats.get(name, ymonth)))
  }

  def searchts(reponame: String, month: String) = Action {
    request =>
      Logger.info("reponame "+reponame +" month "+month)
      Ok(Json.toJson(RepoStats.search(reponame, month)))
  }

  def userActivity(prjName: String) = Action {
    request =>
      Ok(Json.toJson(UserActivity.get(prjName)))
  }
}
