package models.gitdiscover

import com.datastax.driver.core.ResultSet
import database.cdb
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

  def get = {
    process(cdb.client.session.execute("select * from git.toprepos limit 10000;"))
  }

  def process(rs: ResultSet): List[TopRepo] = {
    var tps = Map.empty[String, TopRepo]

    for(r <- rs){
      //tps = TopRepo(r.getString("date"), r.getString("name"), r.getLong("eventstotal"), r.getString("language"))
      if(r.getLong("eventstotal") > 8000) {
        if (tps.get(r.getString("name")).isEmpty) {
          tps = tps + ((r.getString("name"),
            TopRepo(r.getString("date"), r.getString("name"), r.getLong("eventstotal"), r.getString("language"))))
        } else {
          val repo = tps(r.getString("name"))
          tps = tps.updated(repo.name, repo.copy(eventstotal = scala.math.max(repo.eventstotal, r.getLong("eventstotal"))))
        }
      }
      }

    tps.values.toList

  }


}