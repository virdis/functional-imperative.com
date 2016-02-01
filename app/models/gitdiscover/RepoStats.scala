package models.gitdiscover

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.querybuilder.{Clause, QueryBuilder}
import org.joda.time.DateTime
import play.api.libs.json.Json
import scala.collection.JavaConversions._
import database.cdb
import QueryBuilder.{eq => ceq}

/**
  * Created by sandeep on 1/31/16.
  */
case class RepoStats(projectname: String, yrmonth: String,
                     createdAt: String, language: String, eventtype: String, eventcommitter: String)
// x = day : y = noOfCommits on that day
case class GraphView(day: Int, noOfCommit: Int)

object RepoStatsFormat {
  implicit val repostatsFormat = Json.format[RepoStats]
}

object RepoStats {

  def get = {

    //mozilla/browser.html | 2015-01
    val statement  = QueryBuilder.select().all().from("git", "repostats")
      .where(ceq("projectname","mozilla/browser.html")).and(ceq("yrmonth","2015-01")).limit(10)
    process(cdb.client.session.execute(statement))
  }

  def process(rs: ResultSet)  = {
    var rsts = Map.empty[String, GraphView]
    for(r <- rs) {
      if (rsts.get(r.getString("eventtype")).nonEmpty) {
        val gv = rsts(r.getString("eventtype"))
        rsts.updated(r.getString("eventtype"), gv.copy(noOfCommit = gv.noOfCommit + 1))
      } else {
        rsts = rsts + ((r.getString("eventtype"), GraphView(DateTime.parse(r.getString("createdAt")).getDayOfMonth, 1)))
      }
    }
    rsts
  }
}
