import controllers.Posts
import play.api._
import play.api.mvc._
import play.filters.csrf._


object Global extends WithFilters(CSRFFilter()) with GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
    database.db.db

  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
    database.db.db.source.close()
  }
}