package models.gitdiscover

import com.datastax.driver.core.ResultSet
import play.api.libs.json.Json
import scala.collection.JavaConversions._

/**
  * Created by sandeep on 2/4/16.
  */
case class UserActivity(projectname: String, username: String, eventtype: String, count: Long)

object UserActivityFormat {
  implicit val uaFormat = Json.format[UserActivity]
}

object UserActivity {

  def process(rs: ResultSet): List[UserActivity] = {
    var list = List.empty[UserActivity]
    for (r <- rs) {
      list = UserActivity(r.getString("projectname"), r.getString("username"), r.getString("eventtype"), r.getLong("count")) +: list

    }
    list
  }
}