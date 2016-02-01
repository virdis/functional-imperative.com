package controllers

import models.gitdiscover.{TopRepo, TopRepos}
import play.api.mvc.{Action, Controller}
import services.SimpleCConnector

/**
  * Created by sandeep on 1/30/16.
  */
object GitDiscover extends Controller {


  def topProjects = Action { request =>

    //val topRepos = TopRepos( SimpleCConnector.session.execute("select * from toprepos limit 500;") )
    val tes = List(TopRepo("aaa", "bbbb", 20202, "java"),TopRepo("acaa", "bbbbc", 23202, "java"))
    Ok(views.html.topproject(tes))

  }
}
