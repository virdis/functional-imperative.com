package models.gitdiscover

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.querybuilder.{Clause, QueryBuilder}
import play.api.libs.json.Json
import scala.collection.JavaConversions._
import database.cdb
import QueryBuilder.{eq => ceq}

/**
  * Created by sandeep on 1/31/16.
  */
case class RepoStats(projectname: String, yrmonth: String,
                     createdAt: String, language: String, eventtype: String, eventcommitter: String)

object RepoStatsFormat {
  implicit val repostatsFormat = Json.format[RepoStats]
}

object RepoStats {

  def get = {

    //mozilla/browser.html | 2015-01
    val statement  = QueryBuilder.select().all().from("git", "repostats")
      .where(ceq("projectname","mozilla/browser.html")).and(ceq("yrmonth","2015-01")).limit(100)
    process(cdb.client.session.execute(statement))
  }

  def process(rs: ResultSet): List[RepoStats] = {
    var rsts = List.empty[RepoStats]
    for(r <- rs) {
      rsts = RepoStats(r.getString("projectname"), r.getString("yrmonth"), r.getString("createdAt"), r.getString("language"),
       r.getString("eventtype"), r.getString("eventcommitter")) +: rsts
    }
    rsts
  }
}
