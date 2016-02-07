package models.gitdiscover

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.querybuilder.QueryBuilder
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.analysis.core.{StopAnalyzer, StopFilter, WhitespaceAnalyzer, WhitespaceTokenizer}
import org.apache.lucene.analysis.en.{EnglishMinimalStemFilter, EnglishAnalyzer}
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.util.StopwordAnalyzerBase
import play.api.Logger
import play.api.libs.json.Json
import scala.collection.JavaConversions._
import QueryBuilder.{eq => ceq}
import org.apache.lucene.analysis._

import scala.util.control.NonFatal


/**
  * Created by sandeep on 2/5/16.
  */
case class ProjectComments(projectname: String, comments: List[String])
case class MoodCount(text: String, count: Int)
case class GraphItem(list: List[MoodCount])

object PCFormat {
  implicit val pcFormat = Json.format[ProjectComments]
  implicit val gvmcFormat = Json.format[MoodCount]
  implicit val giFormat = Json.format[GraphItem]
}

object ProjectComments {

  val KEYSPACE = "git"
  val TABLE = "projectcomments"

  val angry = "ANGRY"
  val ANGRY_SET = Set(
    "angri","annoi","appal","bitter","cranki","hate","mad"
  )
  val joy = "JOY"
  val JOY_SET = Set(
    "ye","yai","hallelujah", "hurrai", "bingo", "amus", "cheer", "excit", "glad","proud"
  )
  val amusement = "AMUSEMENT"
  val AMUSEMENT_SET = Set(
    "ha","haha","he","hehe","lol"+ "rofl","lmfao","lulz", "lolz", "rotfl", "lawl", "hilari"
  )

  val surprise = "SURPRISED"
  val SURPRISED_SET = Set(
    "yike", "gosh", "baffl", "stump", "surpris" , "shock"
  )

  val swear = "SWEAR"
  val SWEAR_SET = Set(
    "wtf","wth","omfg","hell","ass","bitch",
    "bullshit","bloodi","shit", "shiti","crap",
    "crapi", "fuck","damn","piss", "screw","suck"
  )

  def get(prjName: String): GraphItem = {
    val statement = QueryBuilder.select("projectname","comment").from(KEYSPACE, TABLE)
        .where(ceq("projectname", prjName))
    process(database.cdb.client.session.execute(statement))
  }

  def process(rs: ResultSet): GraphItem = {

    var rsts = List.empty[String]
    var name = ""
    rs.foreach { r =>
      name = r.getString("projectname")
      rsts =  r.getString("comment") +: rsts
    }
    commentStopWords(rsts)
  }

  /*
      Analyze Comments and Tokenize
   */
  def commentStopWords(comments: List[String]): GraphItem = {

    var ang = MoodCount(angry, 0)
    var jy = MoodCount(joy, 0)
    var am = MoodCount(amusement, 0)
    var sur = MoodCount(surprise, 0)
    var swe = MoodCount(swear, 0)

    try {
      comments.foreach { comment =>
        if (comment != null) {
          //println("comment "+comment)
          val englishAnalyzer = new EnglishAnalyzer()

          val ts = englishAnalyzer.tokenStream(null, comment)

          ts.reset()
          while(ts.incrementToken()) {
            val term = ts.getAttribute[CharTermAttribute](classOf[CharTermAttribute]).toString()
            //Logger.info("Term ==== "+term)
            if (ANGRY_SET.contains(term)) ang = ang.copy(count = ang.count + 1)
            else if (JOY_SET.contains(term)) jy = jy.copy(count = jy.count + 1)
            else if (AMUSEMENT_SET.contains(term)) am = am.copy(count = am.count + 1)
            else if (SURPRISED_SET.contains(term)) sur = sur.copy(count = sur.count + 1)
            else if (SWEAR_SET.contains(term)) swe = swe.copy(count = swe.count + 1)

          }
        }

      }
      GraphItem(List(ang, jy, am, sur, swe))
    } catch {
      case NonFatal(ex) =>
        Logger.error("Comment processing failed !!!!!")
        Logger.error(ex.getCause + " message "+ex.getMessage)
        GraphItem(List(ang, jy, am, sur, swe))

    }
  }
}