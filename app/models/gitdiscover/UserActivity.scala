package models.gitdiscover

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.querybuilder.QueryBuilder
import database.cdb
import play.api.libs.json.Json
import scala.collection.JavaConversions._
import QueryBuilder.{eq => ceq}

import scala.collection.mutable

/**
  * Created by sandeep on 2/4/16.
  */
case class UserActivity(projectname: String, username: String, eventtype: String, count: Long)

case class PieChart(label: String, value: Long)

object UserActivityFormat {
  implicit val uaFormat = Json.format[UserActivity]
  implicit val pieFormat = Json.format[PieChart]
}

object UserActivity {

  val KEYSPACE = "git"
  val TABLE = "useractivity"

  def get(prjName: String) = {
    val statement = QueryBuilder.select("username", "count").from(KEYSPACE, TABLE).where(ceq("projectname", prjName))
    process(cdb.client.session.execute(statement))
  }

  def process(rs: ResultSet): List[PieChart] = {
    val list = new mutable.PriorityQueue[PieChart]()(pq)

    for (r <- rs) {
      list += PieChart(r.getString("username"), r.getLong("count"))
    }
    list.toList.take(25)
  }

  object pq extends Ordering[PieChart] {
    override def compare(u1: PieChart, u2: PieChart) = u1.value compare u2.value
  }
}