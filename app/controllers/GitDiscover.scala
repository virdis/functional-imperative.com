package controllers

import models.gitdiscover._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import models.gitdiscover.RepoStatsFormat._
import models.gitdiscover.UserActivityFormat._
import models.gitdiscover.PCFormat._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.collection.mutable
import scala.concurrent.Future
import play.api.cache.Cache
import play.api.Play.current
/**
  * Created by sandeep on 1/30/16.
  */
object GitDiscover extends Controller {


  def topProjects = Action { request =>

    val repos = Cache.getOrElse[List[TopRepo]]("repo") {TopRepos.get}

    Ok(views.html.topproject(repos))

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
