package models.gitdiscover

import com.datastax.driver.core.ResultSet
import play.api.libs.json._
import scala.collection.JavaConversions._

/**
  * Created by sandeep on 1/31/16.
  */
case class TopRepo(date: String, name: String, eventstotal: Long, language: String)


object TopReposFormat {
  implicit val topRepoFmt = Json.format[TopRepo]
}

object TopRepos {
  def apply(rs: ResultSet): List[TopRepo] = {
    var tps = List.empty[TopRepo]
    for(r <- rs){
      tps = TopRepo(r.getString("date"), r.getString("name"), r.getLong("eventstotal"), r.getString("language")) +: tps
    }
    tps

  }
}