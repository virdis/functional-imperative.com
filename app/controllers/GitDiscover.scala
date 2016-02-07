package controllers

import models.gitdiscover.{ProjectComments, UserActivity, RepoStats, TopRepos}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import models.gitdiscover.RepoStatsFormat._
import models.gitdiscover.UserActivityFormat._
import models.gitdiscover.PCFormat._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

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

  def projectTimeSeries(name: String, ymonth: String) = Action.async {
    request =>
      Future(Ok(Json.toJson(RepoStats.get(name, ymonth))))
  }

  def searchts(prjName: String, month: String) = Action {
    request =>
      Logger.info("reponame "+prjName +" month "+month)
      Ok(Json.toJson(RepoStats.search(prjName, month)))
  }

  def userActivity(prjName: String) = Action.async {
    request =>
      Logger.info("Projectname "+prjName)
      Future(
        Ok(Json.toJson(UserActivity.get(prjName)))
      )
  }

  def analyzeComments(prjName: String) = Action.async {
    request =>
      Future(
        Ok(Json.toJson(ProjectComments.get(prjName)))
      )
  }

  def slides = Action {
    request =>
      Ok(views.html.slides())

  }
}
