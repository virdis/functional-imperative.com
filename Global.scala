import database.cdb
import models.gitdiscover.SimpleCassandraClient
import play.api._
import play.api.mvc._
import play.filters.csrf._


object Global extends WithFilters(CSRFFilter()) with GlobalSettings {

  override def beforeStart(app: Application) = {
    Logger.info("Before Application starts")
    try {
      database.db.db.source.close()

    }
    catch { case e:Exception => Logger.info(" *** === *** "+e.printStackTrace()) }
  }

  override def onStart(app: Application) {
    Logger.info("Application has started")
    database.db.db
    cdb.client
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
    database.db.db.source.close()
    cdb.client.shutDownCluster
  }
}