package database

import scala.slick.driver.MySQLDriver.api._
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}
import play.Logger

/**
 * Created by svirdi on 12/31/14.
 */
trait DBHelper {
  lazy val db = {
    Logger.debug("setting up connection pooling")
    val config = new HikariConfig("conf/hikari.properties")
    val ds = new HikariDataSource(config)
    Database.forDataSource(ds)
 }
}

