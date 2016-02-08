package models.gitdiscover

import com.datastax.driver.core.ResultSet
import database.cdb
import play.api.libs.json._
import scala.collection.JavaConversions._

/**
  * Created by sandeep on 1/31/16.
  */
case class TopRepo(name: String, eventstotal: Long, language: String)


object TopReposFormat {
  implicit val topRepoFmt = Json.format[TopRepo]
}

object TopRepos {


  val JAVASCRIPT = "JavaScript"
  val CSHARP = "C#"
  val PYTHON = "Python"
  val RUBY = "Ruby"
  val JAVA = "Java"
  val HASKELL = "Haskell"
  val PHP = "PHP"
  val CPLUSPLUS = "C++"
  val HTML = "HTML"
  val CSS = "CSS"
  val PERL = "Perl"
  val OBJECTIVEC = "Objective-C"
  val GO = "Go"
  val CLOJURE = "Clojure"
  val ANSIC = "C"

  val ALL_LANGS = List(JAVASCRIPT, CSHARP, PYTHON, RUBY, JAVA, HASKELL, PHP,
    CPLUSPLUS, HTML, CSS, PERL, OBJECTIVEC, GO, CLOJURE, ANSIC)

  def get = {
    process(cdb.client.session.execute("select * from git.toprepos limit 10000;"))
  }

  def findMin(m: Map[String,TopRepo]) = {
    var min = 0L
    var minTP: TopRepo = null
    m.keys.foreach {
      k =>
        val t = m(k)
        if (t.eventstotal < min) {
          min = t.eventstotal
          minTP = t
        }
    }
    Option(minTP)
  }
  def process(rs: ResultSet)  = {

    type LANGUAGE = String
    var topLangs = ALL_LANGS.foldLeft(Map.empty[LANGUAGE, Map[String, TopRepo]]) {
      (b, a) =>
        b + ((a, Map.empty[String, TopRepo]))
    }

    for(r <- rs) {
      val currentTR = TopRepo(r.getString("name"), r.getLong("eventstotal"), r.getString("language"))

      if (topLangs.get(r.getString("name")).nonEmpty) {

        val tpMap = topLangs(r.getString("name"))

        if(tpMap.get(currentTR.name).nonEmpty) {
          val tp = tpMap(currentTR.name)
          if (currentTR.eventstotal > tp.eventstotal) {
            topLangs = topLangs.updated(r.getString("name"), tpMap.updated(tp.name, currentTR))
          }
        } else if (tpMap.keys.size >= 10) {
          val minTP: Option[TopRepo] = findMin(tpMap)
          if (minTP.nonEmpty) {
            if (minTP.get.eventstotal < currentTR.eventstotal) {
              val removeMP = tpMap - (minTP.get.name)
              topLangs = topLangs.updated(r.getString("name"), removeMP.updated(currentTR.name, currentTR))

            }
          }

        } else {
          topLangs = topLangs.updated(r.getString("name"), tpMap.updated(currentTR.name, currentTR))
        }
      }

    }

    topLangs
  }

  object repoOrder extends Ordering[TopRepo] {
    override def compare(r1: TopRepo, r2: TopRepo) = r1.eventstotal compare r2.eventstotal
  }

}